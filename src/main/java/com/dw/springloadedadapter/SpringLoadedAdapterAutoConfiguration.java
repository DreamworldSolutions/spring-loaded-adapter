package com.dw.springloadedadapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@ConditionalOnProperty(value = {"com.dw.springloadedadapter.disable"}, havingValue = "false",
    matchIfMissing = true)
public class SpringLoadedAdapterAutoConfiguration {

  private static final Logger logger =
      LoggerFactory.getLogger(SpringLoadedAdapterAutoConfiguration.class);

  @Autowired
  private AutowireCapableBeanFactory beanFactory;

  /**
   * Spring loaded adapter library successfully initialized.
   * 
   */
  public SpringLoadedAdapterAutoConfiguration() {
    logger.info("SpringLoadedAdapterAutoConfiguration() :: "
        + "Initialization of Spring Loadded Adapter...");
  }

  /**
   * Instantiates {@link SpringLoadedAdapterFilter} instance to add,replace or delete file on given
   * path and with files . Filter is invoked for '/spring-loaded/*' url patterns and should have
   * highest precedence.
   * 
   * @return {@link SpringLoadedAdapterFilter} instance.
   */
  @Bean
  public FilterRegistrationBean companyContextFilter() {
    logger.debug("companyContextFilter() :: Initializing Spring loaded filter bean.....");

    SpringLoadedAdapterFilter springLoadedAdapterFilter = new SpringLoadedAdapterFilter();
    beanFactory.autowireBean(springLoadedAdapterFilter);

    FilterRegistrationBean filter = new FilterRegistrationBean(springLoadedAdapterFilter);
    filter.setFilter(springLoadedAdapterFilter);
    filter.addUrlPatterns("/spring-loaded/*");
    filter.setOrder(1);
    return filter;
  }
}