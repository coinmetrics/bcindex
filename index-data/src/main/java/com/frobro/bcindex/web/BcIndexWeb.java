package com.frobro.bcindex.web;

import com.frobro.bcindex.web.configuration.H2Configuration;
import com.frobro.bcindex.web.configuration.SpringProfiles;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main
 */
@SpringBootApplication
public class BcIndexWeb {
    private static final Class<BcIndexWeb> APPLICATION_CLASS = BcIndexWeb.class;

    public static void main( String[] args ) {
        H2Configuration.startIfConfigured();
        setProfileIfNeeded();
        SpringApplication.run(APPLICATION_CLASS, args);
    }

    private static void setProfileIfNeeded() {
      if (SpringProfiles.DEV.shouldActivate()) {
        System.setProperty(SpringProfiles.DEV.getKey(),
            SpringProfiles.DEV.getValue());
      }
    }
}
