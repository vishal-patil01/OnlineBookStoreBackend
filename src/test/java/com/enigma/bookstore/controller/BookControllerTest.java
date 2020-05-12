package com.enigma.bookstore.controller;

import com.enigma.bookstore.dto.BookDTO;
import com.enigma.bookstore.enums.FilterAttributes;
import com.enigma.bookstore.exception.BookException;
import com.enigma.bookstore.model.Book;
import com.enigma.bookstore.service.implementation.BookService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @MockBean
    private BookService bookStoreService;

    List<Book> list = new ArrayList<>();
    BookDTO bookDTO;

    @Test
    void givenRequest_WhenGetResponse_ItShouldReturnBooks() {
        bookDTO = new BookDTO("3436456546654", "Wings Of Fire", "A. P. J. Abdul Kalam", 400.0, 2, "Story Of Abdul Kalam", "/temp/pic01", 2014);
        Book book = new Book(bookDTO);
        list.add(book);
        Page<Book> bookPage = new PageImpl(list);
        when(bookStoreService.fetchBooks(anyString(), anyInt(), any())).thenReturn(bookPage);
        Page<Book> allBooks = bookStoreService.fetchBooks("a", 0, FilterAttributes.DEFAULT);
        Assert.assertEquals(list.get(0).getIsbnNumber(), allBooks.getContent().get(0).getIsbnNumber());
    }

    @Test
    void givenRequest_WhenNoRecordFound_ShouldThrowException() {
        try {
            when(bookStoreService.fetchBooks(anyString(), anyInt(), any())).thenThrow(new BookException("There Are No Books Available"));
            bookStoreService.fetchBooks("zzz", 1, FilterAttributes.DEFAULT);
        } catch (BookException e) {
            Assert.assertSame("There Are No Books Available", e.getMessage());
        }
    }
}
