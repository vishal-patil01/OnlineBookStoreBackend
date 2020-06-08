package com.enigma.bookstore.controller;

import com.enigma.bookstore.dto.ResetPasswordDTO;
import com.enigma.bookstore.dto.Response;
import com.enigma.bookstore.dto.UserLoginDTO;
import com.enigma.bookstore.dto.UserRegistrationDTO;
import com.enigma.bookstore.enums.UserRole;
import com.enigma.bookstore.exception.BookException;
import com.enigma.bookstore.exception.JWTException;
import com.enigma.bookstore.exception.UserException;
import com.enigma.bookstore.model.User;
import com.enigma.bookstore.service.implementation.UserService;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    Gson gson = new Gson();

    @Test
    void givenUserRegistrationData_WhenAllValidationAreTrue_ShouldReturnRegistrationSuccessfulMessage() throws Exception {
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO("Sam", "sam@gmail.com", "Sam@12345", "8855885588", false, UserRole.USER);
        User userDetails = new User(registrationDTO);
        String stringConvertDTO = gson.toJson(userDetails);
        String message = "REGISTRATION SUCCESSFUL";
        when(userService.userRegistration(any(), any())).thenReturn(message);
        MvcResult mvcResult = this.mockMvc.perform(post("/bookstore/user/register").contentType(MediaType.APPLICATION_JSON)
                .content(stringConvertDTO)).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        Response responseDto = gson.fromJson(response, Response.class);
        String responseMessage = responseDto.message;
        Assert.assertEquals(message, responseMessage);
    }

    @Test
    void givenUserRegistrationData_WhenUserIsAlreadyRegistered_ShouldThrowException() throws Exception {
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO("Sam", "sam@gmail.com", "Sam@12345", "8855885588", false, UserRole.USER);
        User userDetails = new User(registrationDTO);
        String stringConvertDTO = gson.toJson(userDetails);
        when(userService.userRegistration(any(), any())).thenThrow(new UserException("User With This Email Address Already Exists"));
        try {
            this.mockMvc.perform(post("/bookstore/user/register").contentType(MediaType.APPLICATION_JSON)
                    .content(stringConvertDTO)).andReturn();
        } catch (BookException e) {
            Assert.assertEquals("User With This Email Address Already Exists", e.getMessage());
        }
    }

    @Test
    void givenEmailForResendVerificationEmail_WhenEmailIsExists_ShouldReturnVerificationEmailSent() throws Exception {
        String message = "Verification Email Has Been Sent";
        when(userService.sendEmailWithTokenLink(any(), any())).thenReturn(message);
        MvcResult mvcResult = this.mockMvc.perform(post("/bookstore/user/resend/email/sam@gmail.com")).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        Response responseDto = gson.fromJson(response, Response.class);
        String responseMessage = responseDto.message;
        Assert.assertEquals(message, responseMessage);
    }

    @Test
    void givenEmailForResendVerificationEmail_WhenEmailNotExists_ShouldThrowEmailNotExists() throws Exception {
        when(userService.sendEmailWithTokenLink(any(), any())).thenThrow(new UserException("Email Address Not Exists"));
        try {
            this.mockMvc.perform(post("/bookstore/user/resend/email/sam@gmail.com").contentType(MediaType.APPLICATION_JSON)).andReturn();
        } catch (UserException e) {
            Assert.assertEquals("Email Address Not Exists", e.getMessage());
        }
    }

    @Test
    void givenValidEmailAndValidToken_WhenEmailIsExists_ShouldReturnEmailAddressVerified() throws Exception {
        String message = "Email Address Verified";
        when(userService.verifyEmail(any())).thenReturn(message);
        MvcResult mvcResult = this.mockMvc.perform(put("/bookstore/user/verify/email/")).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        Response responseDto = gson.fromJson(response, Response.class);
        String responseMessage = responseDto.message;
        Assert.assertEquals(message, responseMessage);
    }

    @Test
    void givenValidEmailAndValidToken_WhenEmailIsExistsAndIsEmailVerified_ShouldReturnEmailAlreadyVerified() throws Exception {
        when(userService.verifyEmail(any())).thenThrow(new UserException("Email Already Verified"));
        try {
            this.mockMvc.perform(post("/bookstore/user/verify/email/sam@gmail.com").contentType(MediaType.APPLICATION_JSON)).andReturn();
        } catch (UserException e) {
            Assert.assertEquals("Email Already Verified", e.getMessage());
        }
    }

    @Test
    void givenUserLoginData_WhenAllValidationAreTrueAndEmailExists_ShouldReturnLoginSuccessfulMessage() throws Exception {
        UserLoginDTO loginDTO = new UserLoginDTO("sam@gmail.com", "Asdfg@123");
        String stringConvertDTO = gson.toJson(loginDTO);
        String message = "Login Successful";
        when(userService.userLogin(any())).thenReturn(message);
        MvcResult mvcResult = this.mockMvc.perform(post("/bookstore/user/login").contentType(MediaType.APPLICATION_JSON)
                .content(stringConvertDTO)).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        Response responseDto = gson.fromJson(response, Response.class);
        String responseMessage = responseDto.message;
        Assert.assertEquals(message, responseMessage);
    }

    @Test
    void givenLoginData_WhenEmailIdIsValidButPasswordIsInvalid_ShouldThrowUserExceptions() throws Exception {
        when(userService.verifyEmail(any())).thenThrow(new UserException("Enter Valid Password"));
        try {
            this.mockMvc.perform(post("/bookstore/user/login").contentType(MediaType.APPLICATION_JSON)).andReturn();
        } catch (UserException e) {
            Assert.assertEquals("Enter Valid Password", e.getMessage());
        }
    }

    @Test
    void givenLoginData_WhenPasswordIsValidButEmailIdIsNotVerified_ShouldThrowUserExceptions() throws Exception {
        when(userService.verifyEmail(any())).thenThrow(new UserException("First Verify Your Email Address"));
        try {
            this.mockMvc.perform(post("/bookstore/user/login").contentType(MediaType.APPLICATION_JSON)).andReturn();
        } catch (UserException e) {
            Assert.assertEquals("First Verify Your Email Address", e.getMessage());
        }
    }

    @Test
    void givenLoginData_WhenEmailIdIsNotRegistered_ShouldThrowUserException() throws Exception {
        when(userService.verifyEmail(any())).thenThrow(new UserException("Account With This Email Address Not Exist"));
        try {
            this.mockMvc.perform(post("/bookstore/user/verify/email/sam@gmail.com").contentType(MediaType.APPLICATION_JSON)).andReturn();
        } catch (UserException e) {
            Assert.assertEquals("Account With This Email Address Not Exist", e.getMessage());
        }
    }

    @Test
    void givenRestPasswordDTO_WhenTokenIsValid_ShouldReturnPasswordChangedSuccessfullyMessage() throws Exception {
        ResetPasswordDTO loginDTO = new ResetPasswordDTO("Asdfg@123");
        String stringConvertDTO = gson.toJson(loginDTO);
        String message = "Password Changed Successfully ";
        when(userService.resetPassword(any(), any())).thenReturn(message);
        MvcResult mvcResult = this.mockMvc.perform(post("/bookstore/user/reset/password/").contentType(MediaType.APPLICATION_JSON)
                .content(stringConvertDTO)).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        Response responseDto = gson.fromJson(response, Response.class);
        String responseMessage = responseDto.message;
        Assert.assertEquals(message, responseMessage);
    }

    @Test
    void givenRestPasswordDTO_WhenTokenIsNotValid_ShouldThrowJwtTokenException() throws Exception {
        try {
            when(userService.resetPassword(any(), any())).thenThrow(new JWTException("Token Expired"));
            this.mockMvc.perform(post("/bookstore/user/reset/password/").contentType(MediaType.APPLICATION_JSON)).andReturn();
        } catch (JWTException e) {
            Assert.assertEquals("Token Expired", e.getMessage());
        }
    }
}