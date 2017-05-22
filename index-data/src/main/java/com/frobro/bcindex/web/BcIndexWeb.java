package com.frobro.bcindex.web;

import com.frobro.bcindex.web.configuration.H2Configuration;
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

        SpringApplication.run(APPLICATION_CLASS, args);
    }
}
