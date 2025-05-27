package com.example.infrastructure.repository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories("com.example.infrastructure.repository")
@EnableTransactionManagement
@EntityScan("com.example.infrastructure.entity")
public class DatabaseConfiguration {
}
