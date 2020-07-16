package com.enigma.bookstore.service;

import com.enigma.bookstore.configuration.ConfigureRabbitMq;
import com.enigma.bookstore.dto.BookDTO;
import com.enigma.bookstore.dto.CartDTO;
import com.enigma.bookstore.dto.UserRegistrationDTO;
import com.enigma.bookstore.enums.UserRole;
import com.enigma.bookstore.exception.BookException;
import com.enigma.bookstore.exception.CartException;
import com.enigma.bookstore.exception.CartItemsException;
import com.enigma.bookstore.exception.UserException;
import com.enigma.bookstore.model.Book;
import com.enigma.bookstore.model.Cart;
import com.enigma.bookstore.model.CartItems;
import com.enigma.bookstore.model.User;
import com.enigma.bookstore.properties.ApplicationProperties;
import com.enigma.bookstore.repository.IBookRepository;
import com.enigma.bookstore.repository.ICartItemsRepository;
import com.enigma.bookstore.repository.ICartRepository;
import com.enigma.bookstore.repository.IUserRepository;
import com.enigma.bookstore.service.implementation.CartService;
import com.enigma.bookstore.util.ITokenGenerator;
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
    ConfigureRabbitMq configureRabbitMq;

    @MockBean
    ApplicationProperties applicationProperties;

    @MockBean
    IBookRepository bookStoreRepository;

    @MockBean
    ICartRepository cartRepository;

    @MockBean
    IUserRepository userRepository;

    @Autowired
    CartService cartService;

    @MockBean
    ITokenGenerator jwtToken;

    CartDTO cartDTO;
    BookDTO bookDTO;
    Cart cart;
    CartItems cartItems;
    List<CartItems> cartItemsList;
    Book book;

    public CartServiceTest() {
        bookDTO = new BookDTO("13665564556", "aaa", "A. P. J. Abdul Kalam", 400.0, 2, "Story Of Abdul Kalam", "/temp/pic01", 2014);
        book = new Book(bookDTO);
        cartDTO = new CartDTO(1, 1);
        cart = new Cart();
        cartItems = new CartItems(cartDTO, book, cart);
        cartItemsList = new ArrayList<>();
        cartItemsList.add(cartItems);
        cart.setCardId(1);
        cart.setCartItems(cartItemsList);
    }

    @Test
    void givenRequestForNewCart_WhenProper_ShouldReturn_CartCreatedSuccessfullyMessage() {
        when(cartRepository.save(any())).thenReturn(cart);
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO("Sam", "dhanashree.bhide3@gmail.com", "Sam@12345", "8855885588", false, UserRole.USER);
        User user=new User(registrationDTO);
        String message = cartService.createCart(user);
        Assert.assertEquals("Cart Created Successfully", message);
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
            when(cartItemsRepository.findByBookIdAndCart_CardId(any(), any())).thenReturn(cartItemsList);
            cartService.addToCart(cartDTO, "authorization");
        } catch (CartItemsException e) {
            Assert.assertEquals("Book Already Exists In Cart", e.getMessage());
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

    @Test
    void givenRequest_WhenGetResponse_ShouldReturnCartData() {
        when(jwtToken.verifyToken(any())).thenReturn(1);
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
        when(cartRepository.findByUserId(any())).thenReturn(Optional.of(cart));
        when(cartItemsRepository.findAllByCart_CardId(1)).thenReturn(cartItemsList);
        List<CartItems> itemsList = cartService.fetchCart("token");
        Assert.assertEquals(cartItemsList, itemsList);
    }

    @Test
    void givenRequestToFetchCartList_WhenCartIsEmpty_ShouldThrowCartItemException() {
        try {
            List<CartItems> cartItems = new ArrayList<>();
            when(jwtToken.verifyToken(any())).thenReturn(1);
            when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
            when(cartRepository.findByUserId(any())).thenReturn(Optional.of(cart));
            when(cartItemsRepository.findAllByCart_CardId(1)).thenReturn(cartItems);
            cartService.fetchCart("token");
        } catch (CartItemsException e) {
            Assert.assertEquals("There Are No Items In A Cart", e.getMessage());
        }
    }

    @Test
    void givenRequestToFetchCartList_WhenCartIsNotFound_ShouldThrowCartException() {
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
    void givenRequestToFetchCartList_WhenUserIsNotFound_ShouldThrowUserException() {
        try {
            when(jwtToken.verifyToken(any())).thenReturn(1);
            when(userRepository.findById(any())).thenThrow(new UserException("User Not Found"));
            cartService.fetchCart("authorization");
        } catch (UserException e) {
            Assert.assertEquals("User Not Found", e.getMessage());
        }
    }

    @Test
    void givenCartId_WhenGetResponse_ShouldReturnCartDeletedSuccessfullyMessage() {
        when(jwtToken.verifyToken(any())).thenReturn(1);
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
        when(cartItemsRepository.findById(1)).thenReturn(Optional.ofNullable(cartItems));
        when(cartRepository.findByUserId(any())).thenReturn(Optional.of(cart));
        String existingBook = cartService.deleteBookFromCart(1, "token");
        Assert.assertEquals("Book Removed From Cart", existingBook);
    }

    @Test
    void givenCartId_WhenItemWithCartItemIdNotExist_ShouldThrowCartItemsException() {
        try {
            when(jwtToken.verifyToken(any())).thenReturn(1);
            when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
            when(cartItemsRepository.findById(1)).thenThrow(new CartItemsException("There Is No Such Item In Cart"));
            when(cartRepository.findByUserId(any())).thenReturn(Optional.of(cart));
            cartService.deleteBookFromCart(1, "token");
        } catch (CartItemsException e) {
            Assert.assertEquals("There Is No Such Item In Cart", e.getMessage());
        }
    }

    @Test
    void givenCartIdAndQuantity_WhenGetResponse_ShouldReturnCartUpdatedSuccessfullyMessage() {
        when(jwtToken.verifyToken(any())).thenReturn(1);
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
        when(cartRepository.findByUserId(any())).thenReturn(Optional.of(cart));
        when(cartItemsRepository.findById(1)).thenReturn(Optional.ofNullable(cartItems));
        when(cartRepository.save(any())).thenReturn(new CartItems());
        String existingBook = cartService.updateCartItemQuantity(1, 2, "token");
        Assert.assertEquals("Cart Updated Successfully", existingBook);
    }

    @Test
    void givenCartIdAndQuantity_WhenQuantityIsInvalid_ShouldReturnThrowBookException() {
        try {
            when(jwtToken.verifyToken(any())).thenReturn(1);
            when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
            when(cartRepository.findByUserId(any())).thenReturn(Optional.of(cart));
            when(cartItemsRepository.findById(1)).thenReturn(Optional.ofNullable(cartItems));
            cartService.updateCartItemQuantity(1, -2, "token");
        } catch (BookException e) {
            Assert.assertEquals("Insufficient Or Invalid Book Quantity", e.getMessage());
        }
    }
}
