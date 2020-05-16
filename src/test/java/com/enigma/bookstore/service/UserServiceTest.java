package com.enigma.bookstore.service;

import com.enigma.bookstore.dto.UserRegistrationDTO;
import com.enigma.bookstore.exception.BookException;
import com.enigma.bookstore.model.User;
import com.enigma.bookstore.repository.IUserRepository;
import com.enigma.bookstore.service.implementation.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @MockBean
    IUserRepository iUserRepository;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserService userService;

    @Test
    void givenUserRegistrationData_WhenEmailIdAllReadyExist_ShouldReturnEmailIdAlreadyPresentMessage() {
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO("Sam", "sam@gmail.com", "Sam@12345", "8855885588", false);
        when(iUserRepository.findByEmail(any())).thenThrow(new BookException("User With This Email Address Already Exists"));
        try {
            userService.userRegistration(registrationDTO);
        } catch (BookException e) {
            Assert.assertEquals("User With This Email Address Already Exists", e.getMessage());
        }
    }


    @Test
    void givenUserRegistrationData_WhenAllValidationAreTrue_ShouldReturnRegistrationSuccessfulMessage() {
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO("Sam", "sam@gmail.com", "Sam@12345", "8855885588", false);
        when(iUserRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(iUserRepository.save(any())).thenReturn(new User());
        String existingBook = userService.userRegistration(registrationDTO);
        Assert.assertEquals("Registration Successful", existingBook);
    }
}
