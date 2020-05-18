package com.enigma.bookstore.controller;

import com.enigma.bookstore.dto.Response;
import com.enigma.bookstore.dto.UserRegistrationDTO;
import com.enigma.bookstore.exception.BookException;
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

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    Gson gson = new Gson();

    @Test
    void givenUserRegistrationData_WhenAllValidationAreTrue_ShouldReturnRegistrationSuccessfulMessage() throws Exception {
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO("Sam", "sam@gmail.com", "Sam@12345", "8855885588", false);
        User userDetails = new User(registrationDTO);
        String stringConvertDTO = gson.toJson(userDetails);
        String message = "REGISTRATION SUCCESSFUL";
        when(userService.userRegistration(any())).thenReturn(message);
        MvcResult mvcResult = this.mockMvc.perform(post("/bookstore/user/register").contentType(MediaType.APPLICATION_JSON)
                .content(stringConvertDTO)).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        Response responseDto = gson.fromJson(response, Response.class);
        String responseMessage = responseDto.message;
        Assert.assertEquals(message, responseMessage);
    }

    @Test
    void givenUserRegistrationData_WhenUserIsAlreadyRegistered_ShouldThrowException() throws Exception {
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO("Sam", "sam@gmail.com", "Sam@12345", "8855885588", false);
        User userDetails = new User(registrationDTO);
        String stringConvertDTO = gson.toJson(userDetails);
        when(userService.userRegistration(any())).thenThrow(new UserException("User With This Email Address Already Exists"));
        try {
            this.mockMvc.perform(post("/bookstore/user/register").contentType(MediaType.APPLICATION_JSON)
                    .content(stringConvertDTO)).andReturn();
        } catch (BookException e) {
            Assert.assertEquals("User With This Email Address Already Exists", e.getMessage());
        }
    }

    @Test
    void givenEmailForResendVerificationEmail_WhenEmailIsExists_ShouldReturnVerificationEmailSent() throws Exception {
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO("Sam", "sam@gmail.com", "Sam@12345", "8855885588", false);
        User userDetails = new User(registrationDTO);
        String stringConvertDTO = gson.toJson(userDetails);
        String message = "Verification Email Has Been Sent";
        when(userService.sendEmailWithTokenLink(any())).thenReturn(message);
        MvcResult mvcResult = this.mockMvc.perform(post("/bookstore/user/resend/email/"+registrationDTO.email).contentType(MediaType.APPLICATION_JSON)
                .content(stringConvertDTO)).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        Response responseDto = gson.fromJson(response, Response.class);
        String responseMessage = responseDto.message;
        Assert.assertEquals(message, responseMessage);
    }
    @Test
    void givenEmailForResendVerificationEmail_WhenEmailNotExists_ShouldThrowEmailNotExists() throws Exception {
        when(userService.userRegistration(any())).thenThrow(new UserException("Email Address Not Exists"));
        try {
            this.mockMvc.perform(post("/bookstore/user/resend/email/sam@gmail.com").contentType(MediaType.APPLICATION_JSON)).andReturn();
        } catch (BookException e) {
            Assert.assertEquals("Email Address Not Exists", e.getMessage());
        }
    }
}

