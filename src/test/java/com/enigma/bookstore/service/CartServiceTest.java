package com.enigma.bookstore.service;

import com.enigma.bookstore.dto.BookDTO;
import com.enigma.bookstore.dto.CartDTO;
import com.enigma.bookstore.exception.BookException;
import com.enigma.bookstore.exception.CartException;
import com.enigma.bookstore.exception.CartItemsException;
import com.enigma.bookstore.exception.UserException;
import com.enigma.bookstore.model.Book;
import com.enigma.bookstore.model.Cart;
import com.enigma.bookstore.model.CartItems;
import com.enigma.bookstore.model.User;
import com.enigma.bookstore.repository.IBookRepository;
import com.enigma.bookstore.repository.ICartItemsRepository;
import com.enigma.bookstore.repository.ICartRepository;
import com.enigma.bookstore.repository.IUserRepository;
import com.enigma.bookstore.service.implementation.CartService;
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
public class CartServiceTest {

    @MockBean
    ICartItemsRepository cartItemsRepository;

    @MockBean
    IBookRepository bookStoreRepository;

    @MockBean
    ICartRepository cartRepository;

    @MockBean
    IUserRepository userRepository;

    @Autowired
    CartService cartService;

    @MockBean
    JWTToken jwtToken;

    CartDTO cartDTO;
    BookDTO bookDTO;
    Cart cart;
    CartItems cartItems;
    List<CartItems> cartList;
    Book book;

    public CartServiceTest() {
        bookDTO = new BookDTO("13665564556", "aaa", "A. P. J. Abdul Kalam", 400.0, 2, "Story Of Abdul Kalam", "/temp/pic01", 2014);
        book = new Book(bookDTO);
        cartDTO = new CartDTO(1, 1);
        cart = new Cart();
        cartItems = new CartItems(cartDTO, book, cart);
        cartList = new ArrayList<>();
        cartList.add(cartItems);
        cart.setCardId(1);
        cart.setCartItems(cartList);
    }

    @Test
    void givenCart_WhenGetResponse_ShouldReturnBookAddedToCartSuccessfullyMessage() {
        when(jwtToken.verifyToken(any())).thenReturn(1);
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
        when(bookStoreRepository.findById(any())).thenReturn(Optional.of(book));
        when(cartRepository.findByUserId(any())).thenReturn(Optional.of(cart));
        when(cartRepository.save(any())).thenReturn(cartItems);
        when(cartItemsRepository.save(any())).thenReturn(cartItems);
        String existingBook = cartService.addToCart(cartDTO, "authorization");
        Assert.assertEquals("Book Added To Cart Successfully", existingBook);
    }

    @Test
    void givenCartDTO_WhenBookAlreadyAddedInCart_ShouldThrowCartItemException() {
        try {
            when(jwtToken.verifyToken(any())).thenReturn(1);
            when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
            when(bookStoreRepository.findById(any())).thenReturn(Optional.of(new Book()));
            when(cartRepository.findByUserId(any())).thenReturn(Optional.of(cart));
            when(cartItemsRepository.findByBookIdAndCart_CardId(any(), any())).thenThrow(new CartItemsException("Book Already Added To Cart"));
            cartService.addToCart(cartDTO, "authorization");
        } catch (CartItemsException e) {
            Assert.assertEquals("Book Already Added To Cart", e.getMessage());
        }
    }

    @Test
    void givenCartDTO_WhenBookIsNotFound_ShouldThrowBookException() {
        try {
            when(jwtToken.verifyToken(any())).thenReturn(1);
            when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
            when(bookStoreRepository.findById(any())).thenThrow(new BookException("Book Not Found"));
            when(cartRepository.findByUserId(any())).thenReturn(Optional.of(cart));
            cartService.addToCart(cartDTO, "authorization");
        } catch (BookException e) {
            Assert.assertEquals("Book Not Found", e.getMessage());
        }
    }

    @Test
    void givenCartDTO_WhenCartIsNotFound_ShouldThrowCartException() {
        try {
            when(jwtToken.verifyToken(any())).thenReturn(1);
            when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
            when(cartRepository.findByUserId(any())).thenThrow(new CartException("Cart Not Found"));
            cartService.addToCart(cartDTO, "authorization");
        } catch (CartException e) {
            Assert.assertEquals("Cart Not Found", e.getMessage());
        }
    }

    @Test
    void givenCartDTO_WhenUserIsNotFound_ShouldThrowUserException() {
        try {
            when(jwtToken.verifyToken(any())).thenReturn(1);
            when(userRepository.findById(any())).thenThrow(new UserException("User Not Found"));
            cartService.addToCart(cartDTO, "authorization");
        } catch (UserException e) {
            Assert.assertEquals("User Not Found", e.getMessage());
        }
    }
}
