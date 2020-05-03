package com.enigma.bookstore.mockitotest.service;

import com.enigma.bookstore.dto.BookDTO;
import com.enigma.bookstore.dto.Response;
import com.enigma.bookstore.exception.AdminException;
import com.enigma.bookstore.model.Book;
import com.enigma.bookstore.repository.IBookStoreRepository;
import com.enigma.bookstore.service.implementation.AdminService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
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

    @InjectMocks
    @Autowired
    AdminService adminService;

    BookDTO bookDTO;

    @Test
    void givenBookDetails_WhenGetResponse_ShouldReturnBookDetails() {
        bookDTO = new BookDTO("136655645456L", "Wings Of Fire", "A. P. J. Abdul Kalam", 400.0, 2, "Story Of Abdul Kalam", "/temp/pic01", 2014);
        Book addedBook = new Book(bookDTO);
        when(bookStoreRepository.save(any())).thenReturn(addedBook);
        Response existingBook = adminService.addBook(bookDTO);
        Assert.assertEquals("Book Added successfully.", existingBook.getMessage());
    }

    @Test
    void givenSameBookDetails_WhenGetResponse_ShouldThrowException() {
        try {
            bookDTO = new BookDTO("136655645456L", "Wings Of Fire", "A. P. J. Abdul Kalam", 400.0, 2, "Story Of Abdul Kalam", "/temp/pic01", 2014);
            Book addedBook = new Book(bookDTO);
            when(bookStoreRepository.save(any())).thenReturn(addedBook);
            when(bookStoreRepository.findByIsbnNumber(bookDTO.getIsbnNumber()))
                    .thenThrow(new AdminException("ISBN Number is already exists.", AdminException.ExceptionType.ISBN_NUMBER_ALREADY_EXISTS));
        } catch (AdminException e) {
            Assert.assertEquals(AdminException.ExceptionType.ISBN_NUMBER_ALREADY_EXISTS, e.type);
        }
    }
}
