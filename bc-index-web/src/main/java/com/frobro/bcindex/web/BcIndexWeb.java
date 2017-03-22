package com.frobro.bcindex.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class BcIndexWeb {
    private static final Class<BcIndexWeb> APPLICATION_CLASS = BcIndexWeb.class;

    public static void main( String[] args ) {
        SpringApplication.run(APPLICATION_CLASS, args);
    }
}
