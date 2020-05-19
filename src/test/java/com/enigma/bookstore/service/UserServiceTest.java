package com.enigma.bookstore.service;

import com.enigma.bookstore.dto.UserRegistrationDTO;
import com.enigma.bookstore.exception.UserException;
import com.enigma.bookstore.model.User;
import com.enigma.bookstore.repository.IUserRepository;
import com.enigma.bookstore.service.implementation.UserService;
import com.enigma.bookstore.util.IMailService;
import com.enigma.bookstore.util.implementation.JWTToken;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @MockBean
    IUserRepository iUserRepository;

    @Autowired
    UserService userService;

    @MockBean
    JWTToken jwtToken;

    @MockBean
    IMailService mailService;

    @Test
    void givenUserRegistrationData_WhenEmailIdAllReadyExist_ShouldReturnEmailIdAlreadyPresentMessage() {
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO("Sam", "sam@gmail.com", "Sam@12345", "8855885588", false);
        when(iUserRepository.findByEmail(any())).thenThrow(new UserException("User With This Email Address Already Exists"));
        try {
            userService.userRegistration(registrationDTO);
        } catch (UserException e) {
            Assert.assertEquals("User With This Email Address Already Exists", e.getMessage());
        }
    }

    @Test
    void givenUserRegistrationData_WhenAllValidationAreTrue_ShouldReturnRegistrationSuccessfulMessage() {
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO("Sam", "sam@gmail.com", "Sam@12345", "8855885588", false);
        when(iUserRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(iUserRepository.save(any())).thenReturn(new User());
        when(jwtToken.generateToken(any(), any())).thenReturn("token");
        String existingBook = userService.userRegistration(registrationDTO);
        Assert.assertEquals("Registration Successful", existingBook);
    }
    @Test
    void givenEmail_WhenEmailAddressExistsInDatabase_ShouldReturnVerificationEmailHasBeenSent() {
        when(iUserRepository.findByEmail(any())).thenReturn(Optional.of(new User()));
        when(mailService.sendEmail(any(), any(), any())).thenReturn("Mail Sent Successfully");
        String existingBook = userService.sendEmailWithTokenLink("Sam@gmail.com");
        Assert.assertEquals("Mail Sent Successfully", existingBook);
    }

    @Test
    void givenEmailForResendVerificationEmail_WhenEmailNotExists_ShouldThrowEmailNotExists() {
        try {
            when(iUserRepository.findByEmail(any())).thenThrow(new UserException("Account With This Email Address Not Exist"));
            when(userService.sendEmailWithTokenLink(any())).thenThrow(new UserException("Account With This Email Address Not Exist"));
            userService.sendEmailWithTokenLink("sam@gmail.com");
        } catch (Exception e) {
            Assert.assertEquals("Account With This Email Address Not Exist", e.getMessage());
        }
    }
}

