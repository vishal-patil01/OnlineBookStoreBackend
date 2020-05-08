package com.enigma.bookstore.mockitotest.service;

import com.enigma.bookstore.dto.BookDTO;
import com.enigma.bookstore.dto.Response;
import com.enigma.bookstore.exception.BookStoreException;
import com.enigma.bookstore.model.Book;
import com.enigma.bookstore.repository.IBookStoreRepository;
import com.enigma.bookstore.service.implementation.AdminService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class AdminServiceTest {

    @MockBean
    IBookStoreRepository bookStoreRepository;

    @Autowired
    AdminService adminService;

    @InjectMocks
    ModelMapper modelMapper;

    BookDTO bookDTO;

    @Test
    void givenBookDetails_WhenGetResponse_ShouldReturnBookDetails() {
        bookDTO = new BookDTO("136655645456L", "Wings Of Fire", "A. P. J. Abdul Kalam", 400.0, 2, "Story Of Abdul Kalam", "/temp/pic01", 2014);
        Book book = modelMapper.map(bookDTO, Book.class);
        when(bookStoreRepository.save(any())).thenReturn(book);
        Response existingBook = adminService.addBook(bookDTO);
        Assert.assertEquals("Book Added successfully.", existingBook.getMessage());
    }

    @Test
    void givenSameBookDetails_WhenGetResponse_ShouldThrowIsbnNumberAlreadyExistsException() {
        try {
            bookDTO = new BookDTO("136655645456L", "Wings Of Fire", "A. P. J. Abdul Kalam", 400.0, 2, "Story Of Abdul Kalam", "/temp/pic01", 2014);
            when(bookStoreRepository.save(any())).thenReturn(bookDTO);
            when(bookStoreRepository.findByIsbnNumber(bookDTO.getIsbnNumber()))
                    .thenThrow(new BookStoreException("ISBN Number is already exists."));
        } catch (BookStoreException e) {
            Assert.assertEquals("ISBN Number is already exists.",e.getMessage());
        }
    }

    @Test
    void givenSameBookDetails_WhenGetResponse_ShouldThrowBookNameAndAuthorNameAlreadyExistsException() {
        try {
            bookDTO = new BookDTO("131655645456L", "Wings Of Fire", "A. P. J. Abdul Kalam", 400.0, 2, "Story Of Abdul Kalam", "/temp/pic01", 2014);
            when(bookStoreRepository.save(any())).thenReturn(bookDTO);
            when(bookStoreRepository.findByBookNameAndAuthorName(bookDTO.getBookName(),bookDTO.getAuthorName()))
                    .thenThrow(new BookStoreException("Book Name and Author Name is already exists."));
        } catch (BookStoreException e) {
            Assert.assertEquals("Book Name and Author Name is already exists.", e.getMessage());
        }
    }
}
