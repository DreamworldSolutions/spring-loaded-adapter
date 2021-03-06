package com.dw.springloadedadapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * {@link SpringLoadedAdapterFilter} instance to add,replace or delete file on given path and with
 * files . Filter is invoked for '/spring-loaded/*' url patterns and should have highest precedence.
 * 
 * @author Jaydeep Kumbhani
 *
 */
public class SpringLoadedAdapterFilter extends GenericFilterBean {

  private static final Logger logger = LoggerFactory.getLogger(SpringLoadedAdapterFilter.class);

  @Autowired
  private RequestParser requestParser;

  @Autowired
  private SpringLoadedAdapterService springLoadedAdapterService;

  /**
   * this filter invoke before security filter and for url "/spring-loaded/*" always given 200 OK.
   * 
   */
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
      FilterChain filterChain) throws IOException, ServletException {

    logger.debug("doFilter() :: Initialing filter");

    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;
    try {
      for (Change change : requestParser.parse(request)) {
        springLoadedAdapterService.put(change);
      }
    } catch (IllegalArgumentException e) {
      response.setStatus(200);
      response.getWriter()
          .write("{\r\n  \"code\":400,\r\n  \"message\":\"" + e.getMessage() + "\"\r\n}");
      return;
    }

    response.setStatus(204);
  }
}
