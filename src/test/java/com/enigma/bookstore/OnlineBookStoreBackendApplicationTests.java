package com.enigma.bookstore;

import com.enigma.bookstore.configuration.ConfigureRabbitMq;
import com.enigma.bookstore.properties.ApplicationProperties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class OnlineBookStoreBackendApplicationTests {
    @MockBean
    ConfigureRabbitMq configureRabbitMq;

    @MockBean
    ApplicationProperties applicationProperties;

    @Test
    void contextLoads() {
    }

}
