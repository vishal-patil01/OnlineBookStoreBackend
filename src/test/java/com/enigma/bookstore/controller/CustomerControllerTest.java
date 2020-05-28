package com.enigma.bookstore.controller;

import com.enigma.bookstore.dto.CustomerDTO;
import com.enigma.bookstore.dto.Response;
import com.enigma.bookstore.enums.AddressType;
import com.enigma.bookstore.exception.CustomerException;
import com.enigma.bookstore.exception.UserException;
import com.enigma.bookstore.model.Customer;
import com.enigma.bookstore.model.User;
import com.enigma.bookstore.service.ICustomerService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ICustomerService customerService;

    Gson gson = new Gson();
    User user = new User();

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
        try {
            when(customerService.fetchCustomerDetails(any(), any())).thenThrow(new CustomerException("There is No CustomerData Available"));
            this.mockMvc.perform(post("/bookstore/customer")).andReturn();
        } catch (CustomerException e) {
            Assert.assertSame("There is No CustomerData Available", e.getMessage());
        }
    }
}

