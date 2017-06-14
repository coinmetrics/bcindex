package com.frobro.bcindex.web.configuration;

import com.frobro.bcindex.web.service.persistence.DbManager;
import org.h2.server.web.WebServlet;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by rise on 5/20/17.
 */
@EntityScan("com.frobro.bcindex")
@EnableJpaRepositories("com.frobro.bcindex")
@ComponentScan("com.frobro.bcindex")
@Configuration
public class H2Configuration implements DisposableBean {
  private static DbManager dbManager;

  public static void startIfConfigured() {
    if (SpringProfiles.DEV.isActive()) {
      startDataBase();
    }
  }

  @Bean
  public ServletRegistrationBean h2serletRistration() {
    ServletRegistrationBean reg = new ServletRegistrationBean(new WebServlet());
    reg.addUrlMappings("/console/*");
    return reg;
  }

  @Override
  public void destroy() throws Exception {
    if (SpringProfiles.DEV.isActive()) {
      stopDataBase();
    }
  }

  private static void startDataBase() {
    dbManager = new DbManager()
        .startDb()
        .startH2Console();
  }

  private void stopDataBase() {
    if (dbManager != null) {
      dbManager.startDb();
      dbManager = null;
    }
  }
}
