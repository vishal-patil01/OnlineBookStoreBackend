package com.enigma.bookstore.mockitotest.controller;

import com.enigma.bookstore.controller.BookStoreController;
import com.enigma.bookstore.dto.BookDTO;
import com.enigma.bookstore.dto.Response;
import com.enigma.bookstore.exception.BookStoreException;
import com.enigma.bookstore.model.Book;
import com.enigma.bookstore.service.implementation.BookStoreService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@WebMvcTest(BookStoreController.class)
public class BookStoreControllerTest {

    @MockBean
    private BookStoreService bookStoreService;

    @InjectMocks
    ModelMapper modelMapper;

    List<Book> list = new ArrayList<>();
    BookDTO bookDTO;
    Book book;

    @Test
    void givenRequest_WhenGetResponse_ItShouldReturnBooks() {
        bookDTO = new BookDTO("3436456546654", "Wings Of Fire", "A. P. J. Abdul Kalam", 400.0, 2, "Story Of Abdul Kalam", "/temp/pic01", 2014);
        book = modelMapper.map(bookDTO, Book.class);
        list.add(book);
        Response response = new Response(list, 200);
        when(bookStoreService.getAllBooks(anyInt(), anyInt())).thenReturn(response);
        Response allBooks = bookStoreService.getAllBooks(1, 12);
        Assert.assertEquals(list.get(0).getIsbnNumber(), allBooks.getBookList().get(0).getIsbnNumber());
    }

    @Test
    void givenRequest_WhenNoRecordFound_ShouldThrowException() {
        try {
            when(bookStoreService.getAllBooks(anyInt(), anyInt())).thenThrow(new BookStoreException("There Are No Books Available"));
            bookStoreService.getAllBooks(1, 12);
        } catch (BookStoreException e) {
            Assert.assertSame("There Are No Books Available", e.getMessage());
        }
    }

    @Test
    void givenRequest_WhenGetResponse_ShouldReturnCount() {
        bookDTO = new BookDTO("3436456546654", "Wings Of Fire", "A. P. J. Abdul Kalam", 400.0, 2, "Story Of Abdul Kalam", "/temp/pic01", 2014);
        book = modelMapper.map(bookDTO, Book.class);
        list.add(book);
        when(bookStoreService.getTotalBookCount()).thenReturn(5);
        int count = bookStoreService.getTotalBookCount();
        Assert.assertEquals(5, count);
    }
}
