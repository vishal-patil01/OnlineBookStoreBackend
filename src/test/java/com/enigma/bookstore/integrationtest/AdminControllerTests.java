package com.enigma.bookstore.integrationtest;

import com.enigma.bookstore.OnlineBookStoreBackendApplication;
import com.enigma.bookstore.dto.BookDTO;
import com.enigma.bookstore.exception.AdminException;
import com.enigma.bookstore.model.Book;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
class AdminControllerTests {
    BookDTO bookDTO;
    Gson gson;
    String jsonDTO;
    Book book;
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
    public void givenBookDetails_WhenAdded_ShouldReturnResponse() {
        bookDTO = new BookDTO("13645245456L", "Wings Of Fire", "Abdul Kalam", 400.0, 2, "Dr. Kalam by narrating his life journey evokes the reader to identify with one’s inner fire and potential, for he was of the firm belief that each one of us was born with the strength and potential to make a tangible change in the world.", "/temp/pic01", 2014);
        book = new Book(bookDTO);
        jsonDTO = gson.toJson(book);
        request = new HttpEntity<>(jsonDTO, headers);
        HttpStatus book1 = this.restTemplate.postForEntity("http://localhost:" + port + "/bookstore/admin/book",
                request, Book.class).getStatusCode();
        Assert.assertEquals(book1, HttpStatus.ALREADY_REPORTED);
    }

    @Test
    public void givenBookDetails_WhenAdded_ShouldThrowIsbnAlreadyException() {
        try {
            bookDTO = new BookDTO("13665245456L", "Wings Of Fire", "Abdul Kalam", 400.0, 2, "Dr. Kalam by narrating his life journey evokes the reader to identify with one’s inner fire and potential, for he was of the firm belief that each one of us was born with the strength and potential to make a tangible change in the world.", "/temp/pic01", 2014);
            book = new Book(bookDTO);
            jsonDTO = gson.toJson(book);
            request = new HttpEntity<>(jsonDTO, headers);
            this.restTemplate.postForEntity("http://localhost:" + port + "/bookstore/admin/book",
                    request, Book.class).getStatusCode();
        } catch (AdminException e) {
            Assert.assertEquals(AdminException.ExceptionType.ISBN_NUMBER_ALREADY_EXISTS, e.type);
        }
    }

    @Test
    public void givenBookDetails_WhenAdded_ShouldThrowBookNameAndAuthorNameAlreadyException() {
        try {
            bookDTO = new BookDTO("13665255456L", "Wings Of Fire", "Abdul Kalam", 400.0, 2, "Dr. Kalam by narrating his life journey evokes the reader to identify with one’s inner fire and potential, for he was of the firm belief that each one of us was born with the strength and potential to make a tangible change in the world.", "/temp/pic01", 2014);
            book = new Book(bookDTO);
            jsonDTO = gson.toJson(book);
            request = new HttpEntity<>(jsonDTO, headers);
            this.restTemplate.postForEntity("http://localhost:" + port + "/bookstore/admin/book",
                    request, Book.class).getStatusCode();
        } catch (AdminException e) {
            Assert.assertEquals(AdminException.ExceptionType.BOOK_AND_AUTHOR_NAME_ALREADY_EXISTS, e.type);
        }
    }
}
