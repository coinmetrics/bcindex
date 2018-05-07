package com.frobro.bcindex.web.configuration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by rise on 4/21/17.
 */
@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages = "com.frobro.bcindex")
@EnableTransactionManagement
@ComponentScan(basePackages = "com.frobro.bcindex")
@EnableJpaRepositories(basePackages = "com.frobro.bcindex")

public class RepoConfig {
}
