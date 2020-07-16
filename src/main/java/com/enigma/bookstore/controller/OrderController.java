package com.enigma.bookstore.controller;

import com.enigma.bookstore.dto.OrderDTO;
import com.enigma.bookstore.dto.Response;
import com.enigma.bookstore.dto.UserRegistrationDTO;
import com.enigma.bookstore.exception.UserException;
import com.enigma.bookstore.model.Orders;
import com.enigma.bookstore.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/bookstore")
public class OrderController {
    @Autowired
    private IOrderService orderService;

    @PostMapping("/order")
    public ResponseEntity<Response> placeOrder(@RequestHeader(value = "token", required = false) String token,@Valid @RequestBody OrderDTO orderDTO,  BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return new ResponseEntity(bindingResult.getAllErrors().get(0).getDefaultMessage(), HttpStatus.ALREADY_REPORTED);
        String orderId = orderService.placeOrder(orderDTO, token);
        Response response = new Response("Order Placed Successfully", orderId, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/order")
    public ResponseEntity<Response> fetchOrderSummary(@RequestHeader(value = "token", required = false) String token) {
        List<Orders> orders = orderService.fetchOrders(token);
        Response response = new Response("Order Data Fetched Successfully", orders, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
