
package com.enigma.bookstore.service;

import com.enigma.bookstore.configuration.ConfigureRabbitMq;
import com.enigma.bookstore.dto.BookDTO;
import com.enigma.bookstore.exception.WishListItemsException;
import com.enigma.bookstore.model.Book;
import com.enigma.bookstore.model.User;
import com.enigma.bookstore.model.WishList;
import com.enigma.bookstore.model.WishListItems;
import com.enigma.bookstore.properties.ApplicationProperties;
import com.enigma.bookstore.repository.IBookRepository;
import com.enigma.bookstore.repository.IUserRepository;
import com.enigma.bookstore.repository.IWishListItemsRepository;
import com.enigma.bookstore.repository.IWishListRepository;
import com.enigma.bookstore.util.implementation.JWTToken;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class WishListServiceTest {

    @MockBean
    IWishListItemsRepository WishListItemsRepository;

    @MockBean
    ConfigureRabbitMq configureRabbitMq;

    @MockBean
    ApplicationProperties applicationProperties;

    @MockBean
    IBookRepository bookStoreRepository;

    @MockBean
    IWishListRepository WishListRepository;

    @MockBean
    IUserRepository userRepository;

    @Autowired
    com.enigma.bookstore.service.implementation.WishListService WishListService;

    @MockBean
    JWTToken jwtToken;

    BookDTO bookDTO;
    com.enigma.bookstore.model.WishList wishList;
    com.enigma.bookstore.model.WishListItems wishListItems;
    List<com.enigma.bookstore.model.WishListItems> wishListItems1;

    public WishListServiceTest() {
        bookDTO = new BookDTO("13665564556", "aaa", "A. P. J. Abdul Kalam", 400.0, 2, "Story Of Abdul Kalam", "/temp/pic01", 2014);
        Book book = new Book(bookDTO);
        wishList = new WishList();
        wishListItems = new WishListItems(book, wishList);
        wishListItems1 = new ArrayList<>();
        wishListItems1.add(wishListItems);
        wishList.setWishId(1);
        wishList.setWishListItems(wishListItems1);
    }

    @Test
    void givenBookToAddInWishList_WhenGetResponse_ShouldReturnBookAddedToWishListSuccessfullyMessage() {
        when(jwtToken.verifyToken(any())).thenReturn(1);
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
        when(bookStoreRepository.findById(any())).thenReturn(Optional.of(new Book()));
        when(WishListRepository.findByUserId(any())).thenReturn(Optional.of(new WishList()));
        when(WishListRepository.save(any())).thenReturn(wishListItems);
        when(WishListItemsRepository.save(any())).thenReturn(new WishListItems());
        String existingBook = WishListService.addToWishList(1, "authorization");
        Assert.assertEquals("Book Added To Wish List Successfully", existingBook);
    }

    @Test
    void givenBookToAddInWishList_WhenBookIsAlreadyExistsInWishList_ShouldThrowWishListException() {
        try {
            when(jwtToken.verifyToken(any())).thenReturn(1);
            when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
            when(bookStoreRepository.findById(any())).thenReturn(Optional.of(new Book()));
            when(WishListRepository.findByUserId(any())).thenReturn(Optional.of(new WishList()));
            when(WishListRepository.save(any())).thenReturn(wishListItems);
            when(WishListItemsRepository.findByBookIdAndWishListWishId(any(), any())).thenReturn(wishListItems1);
            WishListService.addToWishList(1, "authorization");
        } catch (WishListItemsException e) {
            Assert.assertEquals("Book Already Exists In WishList", e.getMessage());
        }
    }

    @Test
    void givenRequest_WhenGetResponse_ShouldReturnWishListData() {
        when(jwtToken.verifyToken(any())).thenReturn(1);
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
        when(WishListRepository.findByUserId(any())).thenReturn(Optional.of(wishList));
        when(WishListItemsRepository.findAllByWishListWishId(1)).thenReturn(wishListItems1);
        List<WishListItems> itemsList = WishListService.fetchWishList("token");
        Assert.assertEquals(wishListItems1, itemsList);
    }

    @Test
    void givenRequest_WhenThereIsNoBookInWishList_ShouldThrowWishListItemException() {
        try {
            when(jwtToken.verifyToken(any())).thenReturn(1);
            when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
            when(WishListRepository.findByUserId(any())).thenReturn(Optional.of(wishList));
            when(WishListItemsRepository.findAllByWishListWishId(1)).thenReturn(new ArrayList<>());
            WishListService.fetchWishList("token");
        } catch (WishListItemsException e) {
            Assert.assertEquals("There Is No Books In WishList", e.getMessage());
        }
    }

    @Test
    void givenBookId_WhenBookExistsInWishList_ShouldReturnBookRemovedFromWishListSuccessfullyMessage() {
        when(jwtToken.verifyToken(any())).thenReturn(1);
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
        when(WishListItemsRepository.findById(1)).thenReturn(Optional.ofNullable(wishListItems));
        when(WishListRepository.findByUserId(any())).thenReturn(Optional.of(wishList));
        String existingBook = WishListService.deleteBookFromWishList(1, "token");
        Assert.assertEquals("Book Removed From WishList", existingBook);
    }

    @Test
    void givenBookId_WhenThereIsNoBookInWishList_ShouldThrowWishListItemException() {
        try {
            when(jwtToken.verifyToken(any())).thenReturn(1);
            when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
            when(WishListRepository.findByUserId(any())).thenReturn(Optional.of(wishList));
            when(WishListItemsRepository.findAllByWishListWishId(1)).thenReturn(new ArrayList<>());
            WishListService.deleteBookFromWishList(1, "token");
        } catch (WishListItemsException e) {
            Assert.assertEquals("There Is No Books In WishList", e.getMessage());
        }
    }
}
