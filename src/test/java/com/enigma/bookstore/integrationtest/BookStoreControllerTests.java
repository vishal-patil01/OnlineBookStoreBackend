package com.enigma.bookstore.integrationtest;

import com.enigma.bookstore.OnlineBookStoreBackendApplication;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;


@SpringBootTest(classes = OnlineBookStoreBackendApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookStoreControllerTests {

    HttpHeaders headers;

    @BeforeEach
    void setUp() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void givenRequest_WhenGetResponse_ShouldReturnBookList() {
        String bookRecords = this.restTemplate.getForEntity("http://localhost:" + port + "/bookstore/book/0/5",
                String.class).getBody();
        Assert.assertTrue(bookRecords.contains("Irresistible"));
    }

    @Test
    public void givenRequest_WhenGetResponse_ShouldReturnTotalBookCount() {
        String bookRecords = this.restTemplate.getForEntity("http://localhost:" + port + "/bookstore/count",
                String.class).getBody();
        Assert.assertEquals(bookRecords, "14");
    }

    @Test
    public void givenRequest_WhenGetResponse_ShouldThrowException() {
        String bookRecords = this.restTemplate.getForEntity("http://localhost:" + port + "/bookstore/book/3/5",
                String.class).getBody();
        Assert.assertTrue(bookRecords.contains("There Are No Books Available"));
    }
}
