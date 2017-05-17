package com.frobro.bcindex.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main
 */
@SpringBootApplication
public class BcIndexWeb {
    private static final Class<BcIndexWeb> APPLICATION_CLASS = BcIndexWeb.class;

    public static void main( String[] args ) {
        SpringApplication.run(APPLICATION_CLASS, args);
    }
}
