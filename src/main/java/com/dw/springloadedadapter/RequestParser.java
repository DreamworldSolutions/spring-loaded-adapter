package com.dw.springloadedadapter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@Component
public class RequestParser {

  private static final Logger logger = LoggerFactory.getLogger(RequestParser.class);
  private static final String KEY_REQUEST = "request";

  @Autowired
  private MultipartResolver multipartResolver;

  @Autowired
  private ObjectMapper objectMapper;


  /**
   * Parsing {@link HttpServletRequest} to {@link List} of {@link Change}.
   * 
   * @param request instance of {@link HttpServletRequest}
   * @return {@link List} of {@link Change}
   */
  public List<Change> parse(HttpServletRequest request) {
    logger.debug("parse() :: parsing request to change dto");

    MultipartHttpServletRequest multipartRequest = multipartResolver.resolveMultipart(request);
    Map<String, MultipartFile> files = multipartRequest.getFileMap();

    List<Request> requests = parseRequests(request);

    if (requests == null || requests.isEmpty()) {
      throw new IllegalArgumentException("Request should not be null or empty");
    }

    List<Change> changes = new ArrayList<Change>();
    for (Request requestDto : requests) {
      validateRequestDto(requestDto);
      changes.add(convertDto(requestDto, files));
    }
    return changes;
  }

  private void validateRequestDto(Request requestDto) {
    if (StringUtils.isBlank(requestDto.getPath())) {
      throw new IllegalArgumentException("Request.path is invalid");
    }

    if (requestDto.getType() == null) {
      throw new IllegalArgumentException("Request.type is invalid");
    }

    if (!requestDto.getType().equals(Type.DELETED) && StringUtils.isBlank(requestDto.getFile())) {
      throw new IllegalArgumentException("Request.file is invalid");
    }
  }

  private Change convertDto(Request requestDto, Map<String, MultipartFile> files) {
    Change change = new Change(requestDto.getPath(), requestDto.getType());
    if (requestDto.getType().equals(Type.DELETED)) {
      return change;
    }

    MultipartFile multipartFile = files.get(requestDto.getFile());
    if (multipartFile == null) {
      throw new IllegalArgumentException("No corresponging file found which"
          + " is define in given request details for file name: " + requestDto.getFile());
    }
    InputStream inputStream = getInputStream(multipartFile);
    change.setInputStream(inputStream);
    change.setOrignalFileName(multipartFile.getOriginalFilename());
    return change;
  }

  private InputStream getInputStream(MultipartFile multipartFile) {
    try {
      return multipartFile.getInputStream();
    } catch (IOException e) {
      throw new RuntimeException("Error while retrieve inputstream from multipart file with name "
          + multipartFile.getOriginalFilename(), e);
    }
  }

  private List<Request> parseRequests(HttpServletRequest request) {
    String requestDtoAsString = request.getParameter(KEY_REQUEST);
    if (StringUtils.isBlank(requestDtoAsString)) {
      throw new IllegalArgumentException("Given request details are invalid");
    }

    try {
      return objectMapper.readValue(requestDtoAsString, new TypeReference<List<Request>>() {});
    } catch (IOException e) {
      throw new IllegalArgumentException("request json not parsable", e);
    }
  }
}
