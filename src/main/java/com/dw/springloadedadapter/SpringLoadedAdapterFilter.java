package com.dw.springloadedadapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * {@link SpringLoadedAdapterFilter} instance to add,replace or delete file on given path and with
 * files . Filter is invoked for '/spring-loaded/*' url patterns and should have highest precedence.
 * 
 * @author Jaydeep Kumbhani
 *
 */
public class SpringLoadedAdapterFilter extends GenericFilterBean {

  private static final Logger logger = LoggerFactory.getLogger(SpringLoadedAdapterFilter.class);

  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
      FilterChain filterChain) throws IOException, ServletException {
    // TODO add, replace or delete updated files

  }
}
