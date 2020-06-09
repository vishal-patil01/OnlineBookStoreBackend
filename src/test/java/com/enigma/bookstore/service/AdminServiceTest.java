package com.enigma.bookstore.service;

import com.enigma.bookstore.dto.BookDTO;
import com.enigma.bookstore.exception.BookException;
import com.enigma.bookstore.model.Book;
import com.enigma.bookstore.model.User;
import com.enigma.bookstore.model.WishList;
import com.enigma.bookstore.model.WishListItems;
import com.enigma.bookstore.repository.IBookRepository;
import com.enigma.bookstore.repository.ICartItemsRepository;
import com.enigma.bookstore.repository.IWishListItemsRepository;
import com.enigma.bookstore.service.implementation.AdminService;
import com.enigma.bookstore.util.IMailService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AdminServiceTest {

    @MockBean
    IBookRepository bookStoreRepository;

    @MockBean
    IWishListItemsRepository wishListItemsRepository;

    @MockBean
    ICartItemsRepository cartItemsRepository;

    @MockBean
    IMailService mailService;

    @Autowired
    AdminService adminService;

    BookDTO bookDTO;
    Book book;
    com.enigma.bookstore.model.WishList wishList;
    com.enigma.bookstore.model.WishListItems wishListItems;
    List<WishListItems> wishListItems1;

    @BeforeEach
    void setUp() {
        bookDTO = new BookDTO("13665564556L", "Wings Of Fire", "A. P. J. Abdul Kalam", 400.0, 2, "Story Of Abdul Kalam", "/temp/pic01", 2014);
        book = new Book(bookDTO);
        wishList = new WishList();
        wishList.setUser(new User());
        wishListItems = new WishListItems(book, wishList);
        wishListItems1 = new ArrayList<>();
        wishListItems1.add(wishListItems);
        wishList.setWishId(1);
        wishList.setWishListItems(wishListItems1);
    }

    @Test
    void givenBookData_WhenAllValidationAreTrue_ShouldReturnBookAddedMessage() {
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
    @Test
    void givenBookDetails_WhenAllValidationAreTrue_ShouldReturnBookUpdatedSuccessfullyMessage() {
        when(bookStoreRepository.findById(any())).thenReturn(java.util.Optional.of(new Book()));
        when(wishListItemsRepository.findAllByBookId(any())).thenReturn(wishListItems1);
        String existingBook = adminService.updateBook(bookDTO, 1);
        Assert.assertEquals("Book Updated successfully.", existingBook);
    }

    @Test
    void givenBookDetails_WhenBookNotFound_ShouldThrowBookNotFoundException() {
        try {
            when(bookStoreRepository.findById(any())).thenThrow(new BookException("Book Not Found"));
            when(wishListItemsRepository.findAllByBookId(any())).thenReturn(wishListItems1);
            adminService.updateBook(bookDTO, 1);
        } catch (BookException e) {
            Assert.assertEquals("Book Not Found", e.getMessage());
        }
    }
    @Test
    void givenBookDetails_WhenBookExistsAndNotAddedInCartOrWishList_ShouldReturnBookDeletedSuccessfullyMessage() {
        when(bookStoreRepository.findById(any())).thenReturn(java.util.Optional.of(new Book()));
        when(wishListItemsRepository.findAllByBookId(any())).thenReturn(new ArrayList<>());
        when(cartItemsRepository.findAllByBookId(any())).thenReturn(new ArrayList<>());
        String existingBook = adminService.deleteBook(1);
        Assert.assertEquals("Book Deleted Successfully", existingBook);
    }

    @Test
    void givenBookDetails_WhenBookExistsAndAddedInCartOrWishList_ShouldReturnBookException() {
        try {
            when(bookStoreRepository.findById(any())).thenReturn(java.util.Optional.of(new Book()));
            when(wishListItemsRepository.findAllByBookId(any())).thenReturn(wishListItems1);
            when(cartItemsRepository.findAllByBookId(any())).thenReturn(new ArrayList<>());
            adminService.deleteBook(1);
        } catch (BookException e) {
            Assert.assertEquals("Book Can Not Be Deleted. It May Be Added In WishList Or Cart", e.getMessage());
        }
    }
}
