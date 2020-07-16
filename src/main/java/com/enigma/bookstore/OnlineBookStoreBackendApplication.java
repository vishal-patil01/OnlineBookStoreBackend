package com.enigma.bookstore;

import com.enigma.bookstore.properties.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
@EnableCaching
public class OnlineBookStoreBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(OnlineBookStoreBackendApplication.class, args);
    }
}
