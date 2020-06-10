package com.enigma.bookstore.service;

import com.enigma.bookstore.configuration.ConfigureRabbitMq;
import com.enigma.bookstore.dto.BookDTO;
import com.enigma.bookstore.enums.FilterAttributes;
import com.enigma.bookstore.exception.BookException;
import com.enigma.bookstore.model.Book;
import com.enigma.bookstore.properties.ApplicationProperties;
import com.enigma.bookstore.rabbitmq.producer.NotificationSender;
import com.enigma.bookstore.repository.IBookRepository;
import com.enigma.bookstore.service.implementation.BookService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.management.Notification;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class BookServiceTest {

    @MockBean
    IBookRepository bookRepository;

    @MockBean
    ConfigureRabbitMq configureRabbitMq;

    @MockBean
    ApplicationProperties applicationProperties;

    @MockBean
    NotificationSender notificationSender;

    @Autowired
    BookService bookService;

    BookDTO bookDTO;
    List<Book> bookList = new ArrayList<>();

    @Test
    void givenRequest_WhenGetResponse_ShouldReturnBookDetailsList() {
        bookDTO = new BookDTO("13665564556L", "aaa", "A. P. J. Abdul Kalam", 400.0, 2, "Story Of Abdul Kalam", "/temp/pic01", 2014);
        Book book = new Book(bookDTO);
        bookList.add(book);
        Pageable pageRequest = PageRequest.of(0, 1, FilterAttributes.LOW_TO_HIGH.sort);
        Page<Book> page = new PageImpl<>(bookList, pageRequest, 10);
        when(bookRepository.fetchBooks(any(), any())).thenReturn(page);
        Page<Book> existingBook = bookService.fetchBooks("a", 1, FilterAttributes.DEFAULT);
        Assert.assertEquals(page.getTotalElements(), existingBook.getTotalElements());
    }

    @Test
    void givenRequest_WhenNoRecordFound_ShouldThrowException() {
        try {
            when(bookRepository.fetchBooks(any(), any())).thenThrow(new BookException("There Are No Books Available"));
            bookService.fetchBooks("zzz", 1, FilterAttributes.DEFAULT);
        } catch (BookException e) {
            Assert.assertSame("There Are No Books Available", e.getMessage());
        }
    }

    @Test
    void givenImageEmail_WhenImageFound_ShouldReturnTrue() throws MalformedURLException {
        Path path = Paths.get(System.getProperty("user.dir") + "/src/main/resources/Images/AnneFrank.jpg");
        Resource resource = new UrlResource(path.toUri());
        when(this.applicationProperties.getUploadDir()).thenReturn("/src/main/resources/Images/");
        Resource imageResponse = bookService.loadImages("AnneFrank.jpg");
        Assert.assertEquals(resource, imageResponse);
    }

    @Test
    void givenImageEmail_WhenImageNotFound_ShouldThrowException() throws MalformedURLException {
        try {
            Path path = Paths.get(System.getProperty("user.dir") + "/src/main/resources/Images/AnneFrank.jpg");
            Resource resource = new UrlResource(path.toUri());
            when(this.applicationProperties.getUploadDir()).thenReturn("/src/main/resources/Images/AnneFrank.jpg");
            Resource imageResponse = bookService.loadImages("1-abc.jpg");
        } catch (BookException ex) {
            Assert.assertEquals("Image not found 1-abc.jpg", ex.getMessage());
        }

    }
}