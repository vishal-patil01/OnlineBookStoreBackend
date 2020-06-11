package com.enigma.bookstore.service;

import com.enigma.bookstore.configuration.ConfigureRabbitMq;
import com.enigma.bookstore.dto.ResetPasswordDTO;
import com.enigma.bookstore.dto.UserLoginDTO;
import com.enigma.bookstore.dto.UserRegistrationDTO;
import com.enigma.bookstore.enums.UserRole;
import com.enigma.bookstore.exception.JWTException;
import com.enigma.bookstore.exception.UserException;
import com.enigma.bookstore.model.User;
import com.enigma.bookstore.properties.ApplicationProperties;
import com.enigma.bookstore.repository.IUserRepository;
import com.enigma.bookstore.service.implementation.UserService;
import com.enigma.bookstore.util.IMailService;
import com.enigma.bookstore.util.ITokenGenerator;
import com.enigma.bookstore.util.implementation.JWTToken;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Autowired
    UserService userService;

    @MockBean
    ConfigureRabbitMq configureRabbitMq;

    @MockBean
    ApplicationProperties applicationProperties;

    @MockBean
    ITokenGenerator jwtToken;

    @MockBean
    IMailService mailService;

    @MockBean
    IUserRepository iUserRepository;

    @MockBean
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    HttpServletRequest httpServletRequest;

    @Test
    void givenUserRegistrationData_WhenEmailIdAllReadyExist_ShouldReturnEmailIdAlreadyPresentMessage() {
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO("Sam", "sam@gmail.com", "Sam@12345", "8855885588", false, UserRole.USER);
        when(iUserRepository.findByEmail(any())).thenThrow(new UserException("User With This Email Address Already Exists"));
        try {
            userService.userRegistration(registrationDTO, any());
        } catch (UserException e) {
            Assert.assertEquals("User With This Email Address Already Exists", e.getMessage());
        }
    }

    @Test
    void givenUserRegistrationData_WhenAllValidationAreTrue_ShouldReturnRegistrationSuccessfulMessage() {
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO("Sam", "sam@gmail.com", "Sam@12345", "8855885588", false, UserRole.USER);
        when(iUserRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(iUserRepository.save(any())).thenReturn(new User());
        when(httpServletRequest.getHeader(any())).thenReturn("verify");
        String existingBook = userService.userRegistration(registrationDTO, httpServletRequest);
        when(iUserRepository.findByEmail(any())).thenReturn(Optional.of(new User()));
        Assert.assertEquals("Registration Successful", existingBook);
    }

    @Test
    void givenEmailForResendVerificationEmail_WhenEmailNotExists_ShouldThrowEmailNotExists() {
        try {
            userService.sendEmailWithTokenLink("sam@gmail.com", httpServletRequest);
        } catch (Exception e) {
            Assert.assertEquals("Account With This Email Address Not Exist", e.getMessage());
        }
    }

    @Test
    void givenUserLoginDTO_WhenAllValidationAreTrue_ShouldReturnLoginSuccessfulMessage() {
        UserLoginDTO userLoginDTO = new UserLoginDTO("sam@gmail.com", "Sam@123");
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO("Sam", "sam@gmail.com", "Sam@12345", "8855885588", false, UserRole.USER);
        User user = new User(registrationDTO);
        user.setEmailVerified(true);
        when(iUserRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(bCryptPasswordEncoder.matches(any(), any())).thenReturn(true);
        when(jwtToken.generateToken(any(), any())).thenReturn("token");
        String existingBook = userService.userLogin(userLoginDTO);
        Assert.assertEquals("token", existingBook);
    }

    @Test
    void givenUserLoginDTO_WhenEmailAddressIsNotExists_ShouldThrowUserException() {
        UserLoginDTO userLoginDTO = new UserLoginDTO("sam@gmail.com", "Sam@123");
        try {
            when(iUserRepository.findByEmail(any())).thenThrow(new UserException("Email Address Not Exists"));
            userService.userLogin(userLoginDTO);
        } catch (UserException e) {
            Assert.assertEquals("Email Address Not Exists", e.getMessage());
        }
    }

    @Test
    void givenUserLoginDTO_WhenEmailAddressIsExistsButPasswordNotMatches_ShouldThrowUserException() {
        try {
            UserLoginDTO userLoginDTO = new UserLoginDTO("sam@gmail.com", "Sam@123");
            UserRegistrationDTO registrationDTO = new UserRegistrationDTO("Sam", "sam@gmail.com", "Sam@12345", "8855885588", false, UserRole.USER);
            User user = new User(registrationDTO);
            user.setEmailVerified(true);
            when(iUserRepository.findByEmail(any())).thenReturn(Optional.of(user));
            when(bCryptPasswordEncoder.matches(any(), any())).thenReturn(false);
            userService.userLogin(userLoginDTO);
        } catch (UserException e) {
            Assert.assertEquals("Enter Valid Password", e.getMessage());
        }
    }

    @Test
    void givenUserLoginDTO_WhenEmailAddressIsNotVerified_ShouldThrowUserException() {
        try {
            UserLoginDTO userLoginDTO = new UserLoginDTO("sam@gmail.com", "Sam@123");
            when(iUserRepository.findByEmail(any())).thenReturn(Optional.of(new User()));
            userService.userLogin(userLoginDTO);
        } catch (UserException e) {
            Assert.assertEquals("First Verify Your Email Address", e.getMessage());
        }
    }

    @Test
    void givenResetPasswordDTO_WhenTokenIsValid_ShouldReturnPasswordChangedSuccessfullyMessage() {
        ResetPasswordDTO resetPasswordDTO = new ResetPasswordDTO("Sam@123");
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO("Sam", "sam@gmail.com", "Sam@12345", "8855885588", false, UserRole.USER);
        User user = new User(registrationDTO);
        user.setEmailVerified(true);
        when(iUserRepository.findById(any())).thenReturn(Optional.of(user));
        when(bCryptPasswordEncoder.matches(any(), any())).thenReturn(true);
        when(jwtToken.generateToken(any(), any())).thenReturn("token");
        String existingBook = userService.resetPassword(resetPasswordDTO, "token");
        Assert.assertEquals("Password Reset Successfully", existingBook);
    }

    @Test
    void givenResetPasswordDTO_WhenEmailAddressIsNotExists_ShouldThrowUserException() {
        try {
            ResetPasswordDTO resetPasswordDTO = new ResetPasswordDTO("Sam@123");
            when(iUserRepository.findById(any())).thenThrow(new UserException("Account With This Email Address Not Exist"));
            userService.resetPassword(resetPasswordDTO, "token");
        } catch (UserException e) {
            Assert.assertEquals("Account With This Email Address Not Exist", e.getMessage());
        }
    }

    @Test
    void givenResetPasswordDTO_WhenEmailAddressExistsButTokenExpired_ShouldThrowJwtException() {
        try {
            ResetPasswordDTO resetPasswordDTO = new ResetPasswordDTO("Sam@123");
            when(iUserRepository.findById(any())).thenReturn(Optional.of(new User()));
            when(jwtToken.verifyToken(any())).thenThrow(new JWTException("Token Expired"));
            userService.resetPassword(resetPasswordDTO, "token");
        } catch (JWTException e) {
            Assert.assertEquals("Token Expired", e.getMessage());
        }
    }
}

