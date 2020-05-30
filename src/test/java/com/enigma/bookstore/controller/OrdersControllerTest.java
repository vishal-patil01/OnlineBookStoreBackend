package com.enigma.bookstore.controller;

import com.enigma.bookstore.dto.Response;
import com.enigma.bookstore.exception.UserException;
import com.enigma.bookstore.model.Customer;
import com.enigma.bookstore.model.Orders;
import com.enigma.bookstore.service.IOrderService;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(OrderController.class)
public class OrdersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IOrderService orderService;

    Gson gson = new Gson();

    @Test
    void givenOrderData_WhenAllValidationAreTrue_ShouldReturnOrderSuccessfulMessage() throws Exception {
        when(orderService.placeOrder(any(), any())).thenReturn(36892);
        MvcResult mvcResult = this.mockMvc.perform(post("/bookstore/order/1500")).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        Response responseDto = gson.fromJson(response, Response.class);
        Double responseMessage = (Double) responseDto.data;
        Assert.assertEquals(36892.0, responseMessage, 0.0);
    }

    @Test
    void givenOrderData_WhenUserNotFound_ShouldThrowUserException() throws Exception {
        when(orderService.placeOrder(any(), any())).thenThrow(new UserException("User Not Found"));
        MvcResult mvcResult = this.mockMvc.perform(post("/bookstore/order/1500")).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        Response responseDto = gson.fromJson(response, Response.class);
        String responseMessage = responseDto.message;
        Assert.assertEquals("User Not Found", responseMessage);
    }

    @Test
    void givenRequest_WhenCartIsNotEmpty_ShouldReturnCartRecords() throws Exception {
        List<Orders> ordersList = new ArrayList<>();
        Orders orders = new Orders();
        orders.setOrderId(123456);
        orders.setCustomer(new Customer());
        ordersList.add(orders);
        when(orderService.fetchOrders(any())).thenReturn(ordersList);
        MvcResult mvcResult = this.mockMvc.perform(get("/bookstore/order")).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        Assert.assertTrue(response.contains("123456"));
    }
}