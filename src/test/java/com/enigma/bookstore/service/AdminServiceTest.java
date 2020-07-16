package com.enigma.bookstore.service;

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
import com.enigma.bookstore.repository.IBookRepository;
import com.enigma.bookstore.repository.ICartItemsRepository;
import com.enigma.bookstore.repository.IUserRepository;
import com.enigma.bookstore.repository.IWishListItemsRepository;
import com.enigma.bookstore.service.implementation.AdminService;
import com.enigma.bookstore.util.IMailService;
import com.enigma.bookstore.util.ITokenGenerator;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.net.HttpURLConnection;
import java.net.URL;
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
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @MockBean
    IWishListItemsRepository wishListItemsRepository;

    @MockBean
    ICartItemsRepository cartItemsRepository;

    @MockBean
    IMailService mailService;

    @Autowired
    AdminService adminService;

    @MockBean
    ITokenGenerator jwtToken;

    @MockBean
    IUserRepository userRepository;

    BookDTO bookDTO;
    Book book;
    WishList wishList;
    WishListItems wishListItems;
    List<WishListItems> wishListItems1;
    User user;

    @BeforeEach
    void setUp() {
        bookDTO = new BookDTO("13665564556L", "Wings Of Fire", "A. P. J. Abdul Kalam", 400.0, 2, "Story Of Abdul Kalam", "/temp/pic01", 2014);
        book = new Book(new BookDTO("13665564556L", "Wings Of Fire", "A. P. J. Abdul Kalam", 400.0, 0, "Story Of Abdul Kalam", "/temp/pic01", 2014));
        wishList = new WishList();
        wishList.setUser(new User());
        wishListItems = new WishListItems(book, wishList);
        wishListItems1 = new ArrayList<>();
        wishListItems1.add(wishListItems);
        wishList.setWishId(1);
        wishList.setWishListItems(wishListItems1);
        user = new User();
        user.setUserRole(UserRole.ADMIN);
    }

    @Test
    void givenBookData_WhenAllValidationAreTrue_ShouldReturnBookAddedMessage() {
        when(jwtToken.verifyToken(any())).thenReturn(1);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        bookDTO = new BookDTO("13665564556L", "Wings Of Fire", "A. P. J. Abdul Kalam", 400.0, 2, "Story Of Abdul Kalam", "/temp/pic01", 2014);
        when(bookStoreRepository.save(any())).thenReturn(new Book());
        String existingBook = adminService.addBook(bookDTO, "token");
        Assert.assertEquals("Book Added successfully.", existingBook);
    }

    @Test
    void givenBookData_WhenUserNotValid_ShouldReturnUserDontHaveAdminPrivilegeException() {
        try {
            when(jwtToken.verifyToken(any())).thenReturn(1);
            user.setUserRole(UserRole.USER);
            when(userRepository.findById(any())).thenReturn(Optional.of(user));
            bookDTO = new BookDTO("13665564556L", "Wings Of Fire", "A. P. J. Abdul Kalam", 400.0, 2, "Story Of Abdul Kalam", "/temp/pic01", 2014);
            when(bookStoreRepository.save(any())).thenReturn(new Book());
            adminService.addBook(bookDTO, "token");
        } catch (UserException e) {
            Assert.assertEquals("User Dont Have Admin Privilege", e.getMessage());
        }
    }

    @Test
    void givenBookData_WhenISBNMatchFound_ShouldReturnISBNNumberIsAlreadyExistsException() {
        try {
            when(jwtToken.verifyToken(any())).thenReturn(1);
            when(userRepository.findById(any())).thenReturn(Optional.of(user));
            bookDTO = new BookDTO("13665564556L", "Wings Of Fire", "A. P. J. Abdul Kalam", 400.0, 2, "Story Of Abdul Kalam", "/temp/pic01", 2014);
            Book book = new Book(bookDTO);
            when(bookStoreRepository.findByIsbnNumber(any())).thenReturn(Optional.of(book));
            adminService.addBook(bookDTO, "token");
        } catch (BookException e) {
            Assert.assertEquals("ISBN Number is already exists.", e.getMessage());
        }
    }

    @Test
    void givenBookData_WhenBookAndAuthorNameMatchFound_ShouldReturnBookNameAndAuthorNameIsAlreadyExistsException() {
        try {
            when(jwtToken.verifyToken(any())).thenReturn(1);
            when(userRepository.findById(any())).thenReturn(Optional.of(user));
            bookDTO = new BookDTO("13665564556L", "Wings Of Fire", "A. P. J. Abdul Kalam", 400.0, 2, "Story Of Abdul Kalam", "/temp/pic01", 2014);
            Book book = new Book(bookDTO);
            when(bookStoreRepository.findByBookNameAndAuthorName(any(), any())).thenReturn(Optional.of(book));
            adminService.addBook(bookDTO, "token");
        } catch (BookException e) {
            Assert.assertEquals("Book Name and Author Name is already exists.", e.getMessage());
        }
    }

    @Test
    void givenSameBookDetails_WhenGetResponse_ShouldThrowIsbnNumberAlreadyExistsException() {
        when(jwtToken.verifyToken(any())).thenReturn(1);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
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
    void givenBookDetails_WhenUserNotValid_ShouldReturnUserDontHaveAdminPrivilegeException() {
        try {
            when(jwtToken.verifyToken(any())).thenReturn(1);
            user.setUserRole(UserRole.USER);
            when(userRepository.findById(any())).thenReturn(Optional.of(user));
            when(bookStoreRepository.findById(any())).thenReturn(Optional.of(book));
            when(wishListItemsRepository.findAllByBookId(any())).thenReturn(wishListItems1);
            adminService.updateBook(bookDTO, 1, "token");
        } catch (UserException e) {
            Assert.assertEquals("User Dont Have Admin Privilege", e.getMessage());
        }
    }

    @Test
    void givenBookDetails_WhenAllValidationAreTrue_ShouldReturnBookUpdatedSuccessfullyMessage() {
        when(jwtToken.verifyToken(any())).thenReturn(1);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(bookStoreRepository.findById(any())).thenReturn(Optional.of(book));
        when(wishListItemsRepository.findAllByBookId(any())).thenReturn(wishListItems1);
        String existingBook = adminService.updateBook(bookDTO, 1, "token");
        Assert.assertEquals("Book Updated successfully.", existingBook);
    }

    @Test
    void givenBookDetails_WhenBookNotFound_ShouldThrowBookNotFoundException() {
        try {
            when(jwtToken.verifyToken(any())).thenReturn(1);
            when(userRepository.findById(any())).thenReturn(Optional.of(user));
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
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(bCryptPasswordEncoder.matches(any(), any())).thenReturn(true);
        when(jwtToken.generateToken(any(), any())).thenReturn("token");
        String existingBook = adminService.adminLogin(userLoginDTO);
        Assert.assertEquals("token", existingBook);
    }

    @Test
    void givenAdminLoginDTO_WhenEmailAddressIsNotExists_ShouldThrowUserException() {
        UserLoginDTO userLoginDTO = new UserLoginDTO("sam@gmail.com", "Sam@123");
        try {
            when(userRepository.findByEmail(any())).thenThrow(new UserException("Email Address Not Exists"));
            adminService.adminLogin(userLoginDTO);
        } catch (UserException e) {
            Assert.assertEquals("Email Address Not Exists", e.getMessage());
        }
    }

    @Test
    void givenAdminLogin_WhenPasswordIncorrect_ShouldThrowEnterValidPasswordException() {
        try {
            UserLoginDTO userLoginDTO = new UserLoginDTO("sam@gmail.com", "Sam@123");
            UserRegistrationDTO registrationDTO = new UserRegistrationDTO("Sam", "sam@gmail.com", "Sam@12345", "8855885588", true, UserRole.ADMIN);
            User user = new User(registrationDTO);
            when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
            when(bCryptPasswordEncoder.matches(any(), any())).thenReturn(false);
            when(jwtToken.generateToken(any(), any())).thenReturn("token");
            adminService.adminLogin(userLoginDTO);
        } catch (UserException e) {
            Assert.assertEquals("Enter Valid Password", e.getMessage());
        }
    }

    @Test
    void givenAdminLoginDTO_WhenEmailAddressIsExistsButRoleIsUser_ShouldThrowUserException() {
        try {
            UserLoginDTO userLoginDTO = new UserLoginDTO("sam@gmail.com", "Sam@123");
            UserRegistrationDTO registrationDTO = new UserRegistrationDTO("Sam", "sam@gmail.com", "Sam@12345", "8855885588", false, UserRole.USER);
            User user = new User(registrationDTO);
            user.setEmailVerified(true);
            when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
            when(bCryptPasswordEncoder.matches(any(), any())).thenReturn(false);
            adminService.adminLogin(userLoginDTO);
        } catch (UserException e) {
            Assert.assertEquals("Your Dont Have Admin Privilege", e.getMessage());
        }
    }

    @Test
    void givenImageToAddAtServerSide_WhenStoredInDirectory_ShouldReturnValidURL() {
        when(jwtToken.verifyToken(any())).thenReturn(1);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        MockMultipartFile multipartFile = new MockMultipartFile("data", "Becoming.jpg", "text/plain", "some xml".getBytes());
        String uploadImagePath = adminService.uploadImage(multipartFile, "token");
        try {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection con = (HttpURLConnection) new URL(uploadImagePath).openConnection();
            con.setRequestMethod("HEAD");
            Assert.assertEquals(con.getResponseCode(), HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void givenImageToAddAtServerSide_WhenUserNotValid_ShouldReturnException() {
        try {
            when(jwtToken.verifyToken(any())).thenReturn(1);
            user.setUserRole(UserRole.USER);
            when(userRepository.findById(any())).thenReturn(Optional.of(user));
            MockMultipartFile multipartFile = new MockMultipartFile("data", "Becoming.jpg", "text/plain", "some xml".getBytes());
            adminService.uploadImage(multipartFile, "token");
        } catch (UserException ue) {
            Assert.assertEquals("User Dont Have Admin Privilege", ue.getMessage());
        }
    }

    @Test
    void givenImageToAddAtServerSide_WhenFileTypeNotValid_ShouldReturnException() {
        try {
            when(jwtToken.verifyToken(any())).thenReturn(1);
            when(userRepository.findById(any())).thenReturn(Optional.of(user));
            MockMultipartFile multipartFile = new MockMultipartFile("data", "Becoming.pdf", "text/plain", "some xml".getBytes());
            adminService.uploadImage(multipartFile, "token");
        } catch (BookException e) {
            Assert.assertEquals("Only Image Files Can Be Uploaded", e.getMessage());
        }
    }
}
