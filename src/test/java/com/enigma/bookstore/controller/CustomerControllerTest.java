package com.enigma.bookstore.controller;

import com.enigma.bookstore.dto.BookDTO;
import com.enigma.bookstore.dto.CustomerDTO;
import com.enigma.bookstore.dto.FeedbackDTO;
import com.enigma.bookstore.dto.Response;
import com.enigma.bookstore.enums.AddressType;
import com.enigma.bookstore.exception.CustomerException;
import com.enigma.bookstore.exception.UserException;
import com.enigma.bookstore.model.Book;
import com.enigma.bookstore.model.Customer;
import com.enigma.bookstore.model.Feedback;
import com.enigma.bookstore.model.User;
import com.enigma.bookstore.service.ICustomerService;
import com.enigma.bookstore.util.implementation.JWTToken;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ICustomerService customerService;

    @MockBean
    JWTToken jwtToken;

    String token;
    Gson gson = new Gson();
    User user = new User();
    HttpHeaders httpHeaders = new HttpHeaders();
    List<FeedbackDTO> allFeedback;

    @BeforeEach
    public void setUp() {
        httpHeaders.set("token", "Qwebst43Y");
        token = "asdgj@123";
        allFeedback = new ArrayList<>();
        FeedbackDTO feedbackDTO = new FeedbackDTO(4, "nice book", "", "Sam");
        allFeedback.add(feedbackDTO);
    }

    @Test
    void givenCustomerData_WhenAllValidationAreTrue_ShouldReturnCustomerDetailsSavedSuccessfullyMessage() throws Exception {
        CustomerDTO customerDTO = new CustomerDTO(425001, "India", "Shiv Colony", "India", "Near Hotel", AddressType.HOME);
        Customer customerDetails = new Customer(customerDTO, user);
        String jsonData = gson.toJson(customerDetails);
        String message = "Customer Details Saved Successfully";
        when(customerService.addCustomerDetails(any(), any())).thenReturn(message);
        MvcResult mvcResult = this.mockMvc.perform(post("/bookstore/customer").contentType(MediaType.APPLICATION_JSON)
                .content(jsonData)).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        Response responseDto = gson.fromJson(response, Response.class);
        String responseMessage = responseDto.message;
        Assert.assertEquals(message, responseMessage);
    }

    @Test
    void givenCustomerDetails_WhenAllValidationAreTrueAndDetailsExistOfGivenAddressType_ShouldReturnCustomerDetailsUpdateSuccessfullyMessage() throws Exception {
        CustomerDTO customerDTO = new CustomerDTO(425001, "India", "Shiv Colony", "India", "Near Hotel", AddressType.HOME);
        Customer customerDetails = new Customer(customerDTO, user);
        String jsonData = gson.toJson(customerDetails);
        String message = "Customer Details Updated Successfully";
        when(customerService.addCustomerDetails(any(), any())).thenReturn(message);
        MvcResult mvcResult = this.mockMvc.perform(post("/bookstore/customer").contentType(MediaType.APPLICATION_JSON)
                .content(jsonData)).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        Response responseDto = gson.fromJson(response, Response.class);
        String responseMessage = responseDto.message;
        Assert.assertEquals(message, responseMessage);
    }

    @Test
    void givenRequest_WhenUserFound_ShouldThrowException() throws Exception {
        try {
            when(customerService.addCustomerDetails(any(), any())).thenThrow(new UserException("User Not Exists"));
            this.mockMvc.perform(post("/bookstore/customer")).andReturn();
        } catch (UserException e) {
            Assert.assertSame("User Not Exists", e.getMessage());
        }
    }

    @Test
    void givenRequest_WhenCustomer_ShouldReturnCustomerDetails() throws Exception {
        CustomerDTO customerDTO = new CustomerDTO(425001, "India", "Shiv Colony", "India", "Near Hotel", AddressType.HOME);
        Customer customerDetails = new Customer(customerDTO, user);
        String jsonData = gson.toJson(customerDetails);
        when(customerService.fetchCustomerDetails(any(), any())).thenReturn(customerDetails);
        MvcResult mvcResult = this.mockMvc.perform(get("/bookstore/customer/" + AddressType.HOME).contentType(MediaType.APPLICATION_JSON)
                .content(jsonData)).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        Response responseDto = gson.fromJson(response, Response.class);
        Assert.assertEquals("Customer Details Fetched Successfully", responseDto.message);
    }

    @Test
    void givenRequestForFetchCustomerDetails_WhenCustomerDetailsNotFound_ShouldThrowException() throws Exception {
        when(customerService.fetchCustomerDetails(any(), any())).thenThrow(new CustomerException("There is No CustomerData Available"));
        MvcResult mvcResult = this.mockMvc.perform(get("/bookstore/customer/" + AddressType.HOME)).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        Response responseDto = gson.fromJson(response, Response.class);
        Assert.assertEquals("There is No CustomerData Available", responseDto.message);
    }

    @Test
    void givenUserToken_WhenIdentifiedAndAddsFeedback_ShouldReturnProperMessage() throws Exception {
        FeedbackDTO feedbackDto = new FeedbackDTO(4, "Book is Interesting", "9876543210", "");
        String feedbackString = new Gson().toJson(feedbackDto);
        when(customerService.addFeedback(any(), any())).thenReturn("Feedback Added Successfully");
        MvcResult result = this.mockMvc.perform(post("/bookstore/comment").content(feedbackString)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(httpHeaders))
                .andReturn();
        Assert.assertEquals("Feedback Added Successfully",
                gson.fromJson(result.getResponse().getContentAsString(), Response.class)
                        .message);
    }

    @Test
    void givenFeedbackMessage_WhenNotProper_ShouldReturnProperMessgae() throws Exception {
        FeedbackDTO feedbackDto = new FeedbackDTO(4, "bad", "9876543210", "");
        String feedbackString = gson.toJson(feedbackDto);
        Mockito.when(customerService.addFeedback(any(), any())).
                thenReturn("Feedback Added Successfully");
        MvcResult result = this.mockMvc.perform(post("/bookstore/comment").
                content(feedbackString)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(httpHeaders))
                .andReturn();
        String response = result.getResponse().getContentAsString();
        Assert.assertEquals("Feedback must include 10-200 characters", response);
    }

    @Test
    void givenFeedback_WhenNoMessageFound_ShouldReturnProperMessgae() throws Exception {
        FeedbackDTO feedbackDto = new FeedbackDTO(4, null, "9876543210", "");
        String feedbackString = new Gson().toJson(feedbackDto);
        when(customerService.addFeedback(any(), any())).thenReturn("Feedback Added Successfully");
        MvcResult result = this.mockMvc.perform(post("/bookstore/comment").
                content(feedbackString)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(httpHeaders))
                .andReturn();
        String response = result.getResponse().getContentAsString();
        Assert.assertEquals("Feedback cannot be null", response);
    }

    @Test
    void givenFeedback_WhenNoRatingFound_ShouldReturnProperMessgae() throws Exception {
        FeedbackDTO feedbackDto = new FeedbackDTO(null, "good book to read", "9876543210", "");
        String feedbackString = new Gson().toJson(feedbackDto);
        when(customerService.addFeedback(any(), any())).thenReturn("Feedback Added Successfully");
        MvcResult result = this.mockMvc.perform(post("/bookstore/comment").
                content(feedbackString)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(httpHeaders))
                .andReturn();
        Assert.assertEquals("Rating cannot be null", result.getResponse().getContentAsString());
    }

    @Test
    void givenIsbnNo_WhenProper_ShouldReturnAllFeedBack() throws Exception {
        BookDTO bookDTO = new BookDTO("836655645456", "Wings Of Fire", "A. P. J. Abdul Kalam", 400.0, 2, "Story Of Abdul Kalam", "/temp/pic01", 2014);
        Book book = new Book(bookDTO);
        BookDTO bookDTO2 = new BookDTO("836655645456", "Bags Of Book", "Sudha Krishnan", 400.0, 2, "Stories", "/temp/pic01", 2014);
        Book book2 = new Book(bookDTO2);
        Feedback feedback = new Feedback(3, 4, "Book is Interesting", book);
        Feedback feedback1 = new Feedback(4, 3, "Nice Book to Read", book2);
        List<Feedback> feedbackList = new ArrayList<>();
        feedbackList.add(feedback);
        feedbackList.add(feedback1);
        Mockito.when(customerService.getAllFeedback(any())).thenReturn(allFeedback);
        MvcResult result = this.mockMvc.perform(get("/bookstore/comments?isbnNumber=")).andReturn();
        Assert.assertEquals(200, result.getResponse().getStatus());
        Assert.assertEquals("Feedback Fetched Successfully", gson.fromJson(result.getResponse().
                getContentAsString(), Response.class).message);
    }

    @Test
    void givenUserToken_WhenIdentified_ShouldReturnProperMessage() throws Exception {
        Mockito.when(customerService.getUserFeedback(any(), any())).
                thenReturn(allFeedback);
        MvcResult result = mockMvc.perform(get("/bookstore/customer/feedback?bookId=1").
                contentType(MediaType.APPLICATION_JSON)
                .headers(httpHeaders))
                .andReturn();
        Assert.assertEquals(200, result.getResponse().getStatus());
    }
}

