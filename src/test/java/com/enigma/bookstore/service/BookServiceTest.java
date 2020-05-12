package com.enigma.bookstore.service;

import com.enigma.bookstore.dto.BookDTO;
import com.enigma.bookstore.enums.FilterAttributes;
import com.enigma.bookstore.model.Book;
import com.enigma.bookstore.repository.IBookRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class BookServiceTest {

    @MockBean
    IBookRepository bookRepository;

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
        Page<Book> existingBook = bookRepository.fetchBooks(pageRequest, "a");
        Assert.assertEquals(page.getTotalElements(), existingBook.getTotalElements());
    }
}