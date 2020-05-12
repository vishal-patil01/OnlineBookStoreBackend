package com.enigma.bookstore.service;

import com.enigma.bookstore.dto.BookDTO;
import com.enigma.bookstore.exception.BookException;
import com.enigma.bookstore.model.Book;
import com.enigma.bookstore.repository.IBookRepository;
import com.enigma.bookstore.service.implementation.AdminService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AdminServiceTest {

    @Mock
    IBookRepository bookStoreRepository;

    @InjectMocks
    AdminService adminService;

    BookDTO bookDTO;


    @Test
    void givenCartData_WhenAllValidationAreTrue_ShouldReturnBookAddedToCartMessage() {
        bookDTO = new BookDTO("13665564556L", "Wings Of Fire", "A. P. J. Abdul Kalam", 400.0, 2, "Story Of Abdul Kalam", "/temp/pic01", 2014);
        when(bookStoreRepository.save(any())).thenReturn(new Book());
        String existingBook = adminService.addBook(bookDTO);
        Assert.assertEquals("Book Added successfully.", existingBook);
    }

    @Test
    void givenSameBookDetails_WhenGetResponse_ShouldThrowIsbnNumberAlreadyExistsException() {
        bookDTO = new BookDTO("13665564556L", "Wings Of Fire", "A. P. J. Abdul Kalam", 400.0, 2, "Story Of Abdul Kalam", "/temp/pic01", 2014);
        when(bookStoreRepository.save(any())).thenReturn(new Book());
        String existingBook = adminService.addBook(bookDTO);
        Assert.assertEquals("Book Added successfully.", existingBook);
    }

    @Test
    void givenSameBookDetails_WhenGetResponse_ShouldThrowBookNameAndAuthorNameAlreadyExistsException() {
        try {
            bookDTO = new BookDTO("131655645456L", "Wings Of Fire", "A. P. J. Abdul Kalam", 400.0, 2, "Story Of Abdul Kalam", "/temp/pic01", 2014);
            when(bookStoreRepository.save(any())).thenReturn(bookDTO);
            when(bookStoreRepository.findByBookNameAndAuthorName(bookDTO.bookName, bookDTO.authorName))
                    .thenThrow(new BookException("Book Name and Author Name is already exists."));
        } catch (BookException e) {
            Assert.assertEquals("Book Name and Author Name is already exists.", e.getMessage());
        }
    }
}
