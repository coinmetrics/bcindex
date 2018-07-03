package com.frobro.bcindex.api;

import com.frobro.bcindex.api.configuration.H2Configuration;
import com.frobro.bcindex.api.configuration.SpringProfiles;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Hello world!
 *
 */
@EntityScan("com.frobro.bcindex")
@EnableJpaRepositories("com.frobro.bcindex")
@ComponentScan("com.frobro.bcindex")
@SpringBootApplication
public class IndexApiApp {
    public static void main( String[] args ) {
        H2Configuration.startIfConfigured();
        SpringProfiles.setProfileIfNeeded();
        SpringApplication.run(IndexApiApp.class, args);
    }
}