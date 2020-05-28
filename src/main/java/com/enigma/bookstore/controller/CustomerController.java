package com.enigma.bookstore.controller;


import com.enigma.bookstore.dto.CustomerDTO;
import com.enigma.bookstore.dto.Response;
import com.enigma.bookstore.enums.AddressType;
import com.enigma.bookstore.model.Customer;
import com.enigma.bookstore.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/bookstore")
public class CustomerController {

    @Autowired
    private ICustomerService customerService;

    @PostMapping("/customer")
    public ResponseEntity<Response> addCustomerDetails(@Valid @RequestBody CustomerDTO customerDTO, @RequestHeader(value = "token", required = false) String token, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return new ResponseEntity(bindingResult.getAllErrors().get(0).getDefaultMessage(), HttpStatus.ALREADY_REPORTED);
        String message = customerService.addCustomerDetails(customerDTO, token);
        Response response = new Response(message, null, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/customer/{addresstype}")
    public ResponseEntity<Response> addCustomerDetails(@PathVariable(name = "addresstype") AddressType addressType, @RequestHeader(value = "token", required = false) String token) {
        Customer customerDetails = customerService.fetchCustomerDetails(addressType, token);
        Response response = new Response("Customer Details Fetched Successfully", customerDetails, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
