package com.frobro.bcindex.web;

import com.frobro.bcindex.web.configuration.H2Configuration;
import com.frobro.bcindex.web.configuration.SpringProfiles;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main
 */
@EntityScan("com.frobro.bcindex")
@EnableJpaRepositories("com.frobro.bcindex")
@ComponentScan("com.frobro.bcindex")
@SpringBootApplication
public class BcIndexWeb {
  private static final Class<BcIndexWeb> APPLICATION_CLASS = BcIndexWeb.class;

  public static void main(String[] args) {
    H2Configuration.startIfConfigured();
    SpringProfiles.setProfileIfNeeded();
    SpringApplication.run(APPLICATION_CLASS, args);
  }
}
