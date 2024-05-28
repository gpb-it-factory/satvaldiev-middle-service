package ru.satvaldiev.middleservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;


@ConfigurationPropertiesScan
@SpringBootApplication
public class MiddleServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MiddleServiceApplication.class, args);
    }
}
