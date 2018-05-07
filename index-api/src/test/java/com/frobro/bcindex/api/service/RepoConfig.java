package com.frobro.bcindex.api.service;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by rise on 4/21/17.
 */
@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages = "com.frobro.bcindex.api")
@EnableTransactionManagement
@ComponentScan(basePackages = "com.frobro.bcindex.api")
@EnableJpaRepositories(basePackages = "com.frobro.bcindex.api")
public class RepoConfig {
}
