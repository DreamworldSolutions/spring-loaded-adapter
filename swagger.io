swagger: "2.0"

################################################################################
#                              API Information                                 #
################################################################################
info:
  version: v0.1.0
  title: Spring loaded adapter APIs
  description:
    Provide an API to upload file changes on server

################################################################################
#                  Host, Base Path, Schemes and Content Types                  #
################################################################################
schemes:
  - http
produces:
  - application/json
consumes:
  - application/json

################################################################################
#                                           Paths                              #
################################################################################
paths:
  /spring-loaded: 
    put: 
      tags:
        - 'Spring-loaded'
      summary: "Uploaded file add/replace with given path."
      description: "
        - Client invoke api to given files and path details as multipart form data. \n
        - From multipart form data, one request key available which containe json string include array of change. \n
        - If change.type is CREATED then change.file add on server`s given change.path location. \n
        - If change.type is UPDATED then change.file replace on server`s given change.path location. 
        
        #### Authorization

        N/A"
        
      consumes: 
        - "multipart/form-data"
      parameters: 
        - in: formData
          name: "Upload new Created file"
          type: file
          description: "- upload file which is created in local target directroy"
        - in: formData
          name: "Upload Updated file"
          type: file
          description: "- upload file which is updated in local target directroy"
        - in: formData
          name: "request"
          type: string
          description: "
          - request provide detail about uploaded files to which files were created/updated/delted on which path location \n
          - request json string available on def seciont and json link is: http://www.jsoneditoronline.org/?id=98110f1b00735c396fc6b718b3e6ee32 "
      responses: 
        204: 
          description: "- If files were successfully changed on given paths."
        404: 
          description: "- If files not found on form-data."
          schema: 
            $ref: "#/definitions/Error"
        400: 
          description: "
          - If request not found in form data or request json string not parsable \n
          - If request array size not match with total no of uploaded file. \n
          - If uploaded file name not fount in request detail."
          schema: 
            $ref: "#/definitions/Error"
  
    delete:
      tags:
        - 'Spring-loaded'
      summary: "Delete file/dir on given path location"
      description: "
        - Delete files and directory from server`s given path location. \n
        - User can give multiple different file location.
        
        #### Authorization

        N/A"
      
      parameters: 
        - in: query
          name: "path"
          required: true
          type: array
          items:
            type: string
          description: "- array of path which want to remove directory/file on server"
          
      responses:
        204: 
          description: "- If files/directroies successfully deleted on server`s given path location."
        404: 
          description: "- if paths have invalid string"
          schema: 
            $ref: "#/definitions/Error"
              

################################################################################
#                                 Definitions                                  #
################################################################################
definitions:

  Error:
    type: object
    properties:
      code:
        type: string
      message:
        type: string
      fields:
        type: string
        
  Request:
    type: object
    properties:
      file:
        type: string
      path:
        type: string
      type:
        type: string
        description: "it is enum to hold CREATED/UPDATED/DELETED constants"
