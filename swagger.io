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
        - From multipart form data, one `request` key available which containe json string include array of change. \n
        - Each uploaded file`s key (form-data map key) must be mention in request details text. and that request details provide detail about the file is create,update or detele on server also provide on which server location to given file replace/add/delete. \n 
        - If change.type is CREATED then change.file add on server`s given change.path location. \n
        - If change.type is UPDATED then change.file replace on server`s given change.path location. \n
        - If change.type is DELETED then only given path in request dto. \n
        - Usre can give multiple path to request for delete files.
        - Request.path is path of file or directroy where chagnes made. \n
        
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
          - If request not found in form data or request json string not parsable or empty array of request \n
          - If uploaded file`s keynot fount in request detail."
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
