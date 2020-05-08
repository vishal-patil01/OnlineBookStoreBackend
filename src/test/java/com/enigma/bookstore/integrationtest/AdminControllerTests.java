package com.enigma.bookstore.integrationtest;

import com.enigma.bookstore.OnlineBookStoreBackendApplication;
import com.enigma.bookstore.dto.BookDTO;
import com.enigma.bookstore.exception.BookStoreException;
import com.enigma.bookstore.model.Book;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = OnlineBookStoreBackendApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@ContextConfiguration
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AdminControllerTests {
    BookDTO bookDTO;
    Gson gson;
    String jsonDTO;
    HttpHeaders headers;
    HttpEntity<String> request;

    @BeforeEach
    void setUp() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        gson = new Gson();
    }

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Order(1)
    public void givenBookDetails_WhenAdded_ShouldReturnResponse() {
        bookDTO = new BookDTO("13605245456L", "Wings Of Fire", "Abdul Kalam", 400.0, 2, "Dr. Kalam by narrating his life journey evokes the reader to identify with one’s inner fire and potential, for he was of the firm belief that each one of us was born with the strength and potential to make a tangible change in the world.", "/temp/pic01", 2014);
        jsonDTO = gson.toJson(bookDTO);
        request = new HttpEntity<>(jsonDTO, headers);
        HttpStatus book1 = this.restTemplate.postForEntity("http://localhost:" + port + "/bookstore/admin/book",
                request, Book.class).getStatusCode();
        Assert.assertEquals(book1, HttpStatus.ALREADY_REPORTED);
    }

    @Test
    @Order(2)
    public void givenBookDetails_WhenAdded_ShouldThrowBookNameAndAuthorNameAlreadyException() {
        try {
            bookDTO = new BookDTO("13645245456L", "Wings Of Fire", "Abdul Kalam", 400.0, 2, "Dr. Kalam by narrating his life journey evokes the reader to identify with one’s inner fire and potential, for he was of the firm belief that each one of us was born with the strength and potential to make a tangible change in the world.", "/temp/pic01", 2014);
            jsonDTO = gson.toJson(bookDTO);
            request = new HttpEntity<>(jsonDTO, headers);
            this.restTemplate.postForEntity("http://localhost:" + port + "/bookstore/admin/book",
                    request, Book.class).getStatusCode();
        } catch (BookStoreException e) {
            Assert.assertEquals("Book Name and Author Name is already exists.",e.getMessage());
        }
    }

    @Test
    @Order(3)
    public void givenBookDetails_WhenAdded_ShouldThrowIsbnAlreadyException() {
        try {
            bookDTO = new BookDTO("13665245456L", "Wings Of Fire", "Abdul Kalam", 400.0, 2, "Dr. Kalam by narrating his life journey evokes the reader to identify with one’s inner fire and potential, for he was of the firm belief that each one of us was born with the strength and potential to make a tangible change in the world.", "/temp/pic01", 2014);
            jsonDTO = gson.toJson(bookDTO);
            request = new HttpEntity<>(jsonDTO, headers);
            this.restTemplate.postForEntity("http://localhost:" + port + "/bookstore/admin/book",
                    request, Book.class).getStatusCode();
        } catch (BookStoreException e) {
            Assert.assertEquals("ISBN Number is already exists.",e.getMessage());
        }
    }
}
