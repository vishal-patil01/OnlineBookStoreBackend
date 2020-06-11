package com.enigma.bookstore.service;

import com.enigma.bookstore.configuration.ConfigureRabbitMq;
import com.enigma.bookstore.dto.CustomerDTO;
import com.enigma.bookstore.enums.AddressType;
import com.enigma.bookstore.exception.CustomerException;
import com.enigma.bookstore.exception.JWTException;
import com.enigma.bookstore.exception.UserException;
import com.enigma.bookstore.model.Customer;
import com.enigma.bookstore.model.User;
import com.enigma.bookstore.properties.ApplicationProperties;
import com.enigma.bookstore.repository.ICustomerRepository;
import com.enigma.bookstore.repository.IUserRepository;
import com.enigma.bookstore.service.implementation.CustomerService;
import com.enigma.bookstore.util.ITokenGenerator;
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
public class CustomerServiceTest {

    @MockBean
    ICustomerRepository customerRepository;

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

    CustomerDTO customerDTO;
    List<Customer> customersList = new ArrayList<>();

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
}

