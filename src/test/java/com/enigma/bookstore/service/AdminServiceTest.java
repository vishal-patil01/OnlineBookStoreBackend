package com.enigma.bookstore.service;

import com.enigma.bookstore.configuration.ConfigureRabbitMq;
import com.enigma.bookstore.dto.BookDTO;
import com.enigma.bookstore.dto.UserLoginDTO;
import com.enigma.bookstore.dto.UserRegistrationDTO;
import com.enigma.bookstore.enums.UserRole;
import com.enigma.bookstore.exception.BookException;
import com.enigma.bookstore.exception.UserException;
import com.enigma.bookstore.model.Book;
import com.enigma.bookstore.model.User;
import com.enigma.bookstore.model.WishList;
import com.enigma.bookstore.model.WishListItems;
import com.enigma.bookstore.properties.ApplicationProperties;
import com.enigma.bookstore.rabbitmq.producer.NotificationSender;
import com.enigma.bookstore.repository.IBookRepository;
import com.enigma.bookstore.repository.ICartItemsRepository;
import com.enigma.bookstore.repository.IUserRepository;
import com.enigma.bookstore.repository.IWishListItemsRepository;
import com.enigma.bookstore.service.implementation.AdminService;
import com.enigma.bookstore.util.EmailTemplateGenerator;
import com.enigma.bookstore.util.IMailService;
import com.enigma.bookstore.util.ITokenGenerator;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AdminServiceTest {

    @MockBean
    IBookRepository bookStoreRepository;

    @MockBean
    ConfigureRabbitMq configureRabbitMq;

    @MockBean
    NotificationSender notificationSender;

    @MockBean
    IUserRepository iUserRepository;

    @MockBean
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @MockBean
    ApplicationProperties applicationProperties;

    @MockBean
    IWishListItemsRepository wishListItemsRepository;

    @MockBean
    ICartItemsRepository cartItemsRepository;

    @MockBean
    IMailService mailService;

    @MockBean
    EmailTemplateGenerator emailTemplateGenerator;

    @Autowired
    AdminService adminService;

    @MockBean
    ITokenGenerator jwtToken;

    BookDTO bookDTO;
    Book book;
    com.enigma.bookstore.model.WishList wishList;
    com.enigma.bookstore.model.WishListItems wishListItems;
    List<WishListItems> wishListItems1;
    User user;

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
        UserRegistrationDTO adminDTO = new UserRegistrationDTO("Admin", "admin@gmail.com", "Sam@12345", "9874563210", true, UserRole.ADMIN);
        user = new User(adminDTO);
    }

    @Test
    void givenBookData_WhenAllValidationAreTrue_ShouldReturnBookAddedMessage() {
        when(jwtToken.verifyToken(any())).thenReturn(1);
        when(iUserRepository.findById(any())).thenReturn(Optional.of(user));
        bookDTO = new BookDTO("13665564556L", "Wings Of Fire", "A. P. J. Abdul Kalam", 400.0, 2, "Story Of Abdul Kalam", "/temp/pic01", 2014);
        when(bookStoreRepository.save(any())).thenReturn(new Book());
        String existingBook = adminService.addBook(bookDTO, "token");
        Assert.assertEquals("Book Added successfully.", existingBook);
    }

    @Test
    void givenSameBookDetails_WhenGetResponse_ShouldThrowIsbnNumberAlreadyExistsException() {
        when(jwtToken.verifyToken(any())).thenReturn(1);
        when(iUserRepository.findById(any())).thenReturn(Optional.of(user));
        bookDTO = new BookDTO("13665564556L", "Wings Of Fire", "A. P. J. Abdul Kalam", 400.0, 2, "Story Of Abdul Kalam", "/temp/pic01", 2014);
        when(bookStoreRepository.save(any())).thenReturn(new Book());
        String existingBook = adminService.addBook(bookDTO, "token");
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
        when(jwtToken.verifyToken(any())).thenReturn(1);
        when(iUserRepository.findById(any())).thenReturn(Optional.of(user));
        when(bookStoreRepository.findById(any())).thenReturn(java.util.Optional.of(new Book()));
        when(wishListItemsRepository.findAllByBookId(any())).thenReturn(wishListItems1);
        when(emailTemplateGenerator.getBookAvailableInStockTemplate(any())).thenReturn("Shop Now Book Is Available");
        String existingBook = adminService.updateBook(bookDTO, 1, "token");
        Assert.assertEquals("Book Updated successfully.", existingBook);
    }

    @Test
    void givenBookDetails_WhenBookNotFound_ShouldThrowBookNotFoundException() {
        try {
            when(jwtToken.verifyToken(any())).thenReturn(1);
            when(iUserRepository.findById(any())).thenReturn(Optional.of(user));
            when(bookStoreRepository.findById(any())).thenThrow(new BookException("Book Not Found"));
            when(wishListItemsRepository.findAllByBookId(any())).thenReturn(wishListItems1);
            adminService.updateBook(bookDTO, 1, "token");
        } catch (BookException e) {
            Assert.assertEquals("Book Not Found", e.getMessage());
        }
    }

    @Test
    void givenAdminLoginDTO_WhenAllValidationAreTrue_ShouldReturnLoginSuccessfulMessage() {
        UserLoginDTO userLoginDTO = new UserLoginDTO("sam@gmail.com", "Sam@123");
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO("Sam", "sam@gmail.com", "Sam@12345", "8855885588", true, UserRole.ADMIN);
        User user = new User(registrationDTO);
        when(iUserRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(bCryptPasswordEncoder.matches(any(), any())).thenReturn(true);
        when(jwtToken.generateToken(any(), any())).thenReturn("token");
        String existingBook = adminService.adminLogin(userLoginDTO);
        Assert.assertEquals("token", existingBook);
    }

    @Test
    void givenAdminLoginDTO_WhenEmailAddressIsNotExists_ShouldThrowUserException() {
        UserLoginDTO userLoginDTO = new UserLoginDTO("sam@gmail.com", "Sam@123");
        try {
            when(iUserRepository.findByEmail(any())).thenThrow(new UserException("Email Address Not Exists"));
            adminService.adminLogin(userLoginDTO);
        } catch (UserException e) {
            Assert.assertEquals("Email Address Not Exists", e.getMessage());
        }
    }

    @Test
    void givenAdminLoginDTO_WhenEmailAddressIsExistsButRoleIsUser_ShouldThrowUserException() {
        try {
            UserLoginDTO userLoginDTO = new UserLoginDTO("sam@gmail.com", "Sam@123");
            UserRegistrationDTO registrationDTO = new UserRegistrationDTO("Sam", "sam@gmail.com", "Sam@12345", "8855885588", false, UserRole.USER);
            User user = new User(registrationDTO);
            user.setEmailVerified(true);
            when(iUserRepository.findByEmail(any())).thenReturn(Optional.of(user));
            when(bCryptPasswordEncoder.matches(any(), any())).thenReturn(false);
            adminService.adminLogin(userLoginDTO);
        } catch (UserException e) {
            Assert.assertEquals("Your Dont Have Admin Privilege", e.getMessage());
        }
    }
}
