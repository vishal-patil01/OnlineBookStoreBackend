package com.enigma.bookstore;

import com.enigma.bookstore.properties.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
public class OnlineBookStoreBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(OnlineBookStoreBackendApplication.class, args);
    }
}
