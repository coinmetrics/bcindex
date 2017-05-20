package com.frobro.bcindex.web.configuration;

import com.frobro.bcindex.web.service.persistence.DbManager;
import org.h2.server.web.WebServlet;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static com.frobro.bcindex.web.configuration.SpringProfiles.DEV;
/**
 * Created by rise on 5/20/17.
 */
@Configuration
public class H2Configuration implements DisposableBean {

  private static final String DB_ENV = System.getProperty("spring.profiles.active");

  private static DbManager dbManager;

  public static void startIfConfigured() {
    if (isDevEnv()) {
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
    if (isDevEnv()) {
      stopDataBase();
    }
  }

  private static boolean isDevEnv() {
    return DB_ENV != null
      && SpringProfiles.DEV.getValue().equals(DB_ENV);
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
