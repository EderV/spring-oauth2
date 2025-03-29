package com.evm.oauth2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    @Value("${variableA}")
    private String variableA;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) {
        log.info("Variable A: {}", variableA);

        log.error("DB url: {}", System.getenv("MYSQL_URL"));
        log.error("DB user: {}", System.getenv("MYSQL_USER"));
        log.error("DB password: {}", System.getenv("MYSQL_PASS"));
    }
}
