package com.enigma.bookstore.service;

import com.enigma.bookstore.configuration.ConfigureRabbitMq;
import com.enigma.bookstore.dto.BookDTO;
import com.enigma.bookstore.dto.CustomerDTO;
import com.enigma.bookstore.dto.FeedbackDTO;
import com.enigma.bookstore.dto.UserRegistrationDTO;
import com.enigma.bookstore.enums.AddressType;
import com.enigma.bookstore.enums.UserRole;
import com.enigma.bookstore.exception.CustomerException;
import com.enigma.bookstore.exception.JWTException;
import com.enigma.bookstore.exception.UserException;
import com.enigma.bookstore.model.Book;
import com.enigma.bookstore.model.Customer;
import com.enigma.bookstore.model.Feedback;
import com.enigma.bookstore.model.User;
import com.enigma.bookstore.properties.ApplicationProperties;
import com.enigma.bookstore.repository.IBookRepository;
import com.enigma.bookstore.repository.ICustomerRepository;
import com.enigma.bookstore.repository.IFeedbackRepository;
import com.enigma.bookstore.repository.IUserRepository;
import com.enigma.bookstore.service.implementation.CustomerService;
import com.enigma.bookstore.util.ITokenGenerator;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CustomerServiceTest {

    @MockBean
    ICustomerRepository customerRepository;

    @MockBean
    IBookRepository bookRepository;

    @MockBean
    IFeedbackRepository feedbackRepository;

    @MockBean
    ConfigureRabbitMq configureRabbitMq;

    @MockBean
    ApplicationProperties applicationProperties;

    @MockBean
    IUserRepository userRepository;

    @Autowired
    CustomerService customerService;

    @MockBean
    ITokenGenerator jwtToken;

    String token;

    CustomerDTO customerDTO;
    List<Customer> customersList = new ArrayList<>();
    HttpHeaders httpHeaders=new HttpHeaders();
    List<FeedbackDTO> allFeedback;

    @BeforeEach
    public void setUp() {
        httpHeaders.set("token","Qwebst43Y");
        token="asdgj@123";
        allFeedback = new ArrayList<>();
        FeedbackDTO feedbackDTO = new FeedbackDTO(4, "nice book", "", "Sam");
        allFeedback.add(feedbackDTO);
    }

    public CustomerServiceTest() {
        customerDTO = new CustomerDTO(425001, "USA", "Street No 34", "London", "Near Hotel", AddressType.HOME);
        Customer customer = new Customer(customerDTO, new User());
        customersList.add(customer);
    }

    @Test
    void givenCustomerAddressType_WhenAddressTypeIsPresent_ShouldReturnCustomerDetailsUpdatedSuccessful() {
        when(jwtToken.verifyToken(any())).thenReturn(1);
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
        when(customerRepository.findByUserIdAndAndCustomerAddressType(any(), any())).thenReturn(customersList);
        String existingBook = customerService.addCustomerDetails(customerDTO, "authorization");
        Assert.assertEquals("Customer Details Updated Successfully", existingBook);
    }

    @Test
    void givenCustomerAddressType_WhenAddressTypeIsNotPresent_ShouldReturnCustomerDetailsSavedSuccessfully() {
        List<Customer> customersList = new ArrayList<>();
        when(jwtToken.verifyToken(any())).thenReturn(1);
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
        when(customerRepository.findByUserIdAndAndCustomerAddressType(any(), any())).thenReturn(customersList);
        String existingBook = customerService.addCustomerDetails(customerDTO, "authorization");
        Assert.assertEquals("Customer Details Saved Successfully", existingBook);
    }

    @Test
    void givenCustomerAddressType_WheUserNotPresent_ShouldThrowUserException() {
        try {
            when(jwtToken.verifyToken(any())).thenReturn(1);
            when(userRepository.findById(any())).thenThrow(new UserException("User Not Found"));
            customerService.addCustomerDetails(customerDTO, "authorization");
        } catch (UserException e) {
            Assert.assertEquals("User Not Found", e.getMessage());
        }
    }

    @Test
    void givenCustomerAddressType_WheTokenIsExpired_ShouldThrowJwtException() {
        try {
            when(jwtToken.verifyToken(any())).thenThrow(new JWTException("Token Expired"));
            customerService.addCustomerDetails(customerDTO, "authorization");
        } catch (JWTException e) {
            Assert.assertEquals("Token Expired", e.getMessage());
        }
    }

    @Test
    void givenRequest_WhenCustomerDetailsAreAvailable_ShouldReturnCustomerDetails() {
        when(jwtToken.verifyToken(any())).thenReturn(1);
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
        when(customerRepository.findByUserIdAndAndCustomerAddressType(any(), any())).thenReturn(customersList);
        Customer existingBook = customerService.fetchCustomerDetails(AddressType.HOME, "authorization");
        Assert.assertEquals(customersList.get(0), existingBook);
    }

    @Test
    void givenCustomerAddressType_WheCustomerDetailsNotPresent_ShouldThrowUserException() {
        try {
            when(jwtToken.verifyToken(any())).thenReturn(1);
            when(customerRepository.findByUserIdAndAndCustomerAddressType(any(),any())).thenThrow(new CustomerException("There is No CustomerData Available"));
            customerService.fetchCustomerDetails(AddressType.HOME, "authorization");
        } catch (CustomerException e) {
            Assert.assertEquals("There is No CustomerData Available", e.getMessage());
        }
    }

    @Test
    void givenFeedback_WhenProper_ShouldReturnTrue(){
        FeedbackDTO feedbackDto=new FeedbackDTO(3,"Good Book","9765432133","");
        String isbn = feedbackDto.isbNumber;
        BookDTO bookDto = new BookDTO("998542365", "Into the air","Jack", 20, 5,
                "About an adventure", "sdfsfd", 2014);
        Book book = new Book(bookDto);
        book.setId(3);
        Feedback feedback=new Feedback(5,3,"Good Book",book);
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO("Sam", "sam@gmail.com", "Sam@12345", "8855885588", false, UserRole.USER);
        User user = new User(registrationDTO);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(jwtToken.verifyToken(any())).thenReturn(1);
        when(bookRepository.findByIsbnNumber(isbn)).thenReturn(Optional.of(book));
        when(feedbackRepository.save(any())).thenReturn(feedback);
        String response = customerService.addFeedback(token,feedbackDto);
        Assert.assertEquals("Thank you For your Feedback ",response);
    }
}

