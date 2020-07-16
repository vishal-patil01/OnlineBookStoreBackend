package com.enigma.bookstore.controller;


import com.enigma.bookstore.dto.CustomerDTO;
import com.enigma.bookstore.dto.FeedbackDTO;
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
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/bookstore")
public class CustomerController {

    @Autowired
    private ICustomerService customerService;

    @PostMapping("/customer")
    public ResponseEntity<Response> addCustomerDetails( @RequestHeader(value = "token", required = false) String token,@Valid @RequestBody CustomerDTO customerDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return new ResponseEntity(bindingResult.getAllErrors().get(0).getDefaultMessage(), HttpStatus.ALREADY_REPORTED);
        String message = customerService.addCustomerDetails(customerDTO, token);
        Response response = new Response(message, null, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/customer/{addresstype}")
    public ResponseEntity<Response> fetchCustomerDetails(@PathVariable(name = "addresstype") AddressType addressType, @RequestHeader(value = "token", required = false) String token) {
        Customer customerDetails = customerService.fetchCustomerDetails(addressType, token);
        Response response = new Response("Customer Details Fetched Successfully", customerDetails, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/comment")
    public ResponseEntity<Response> addUserFeedback(@Valid @RequestBody FeedbackDTO feedbackDto, BindingResult bindingResult, @RequestHeader String token) {
        if (bindingResult.hasErrors())
            return new ResponseEntity(bindingResult.getAllErrors().get(0).getDefaultMessage(), HttpStatus.ALREADY_REPORTED);
        String feedbackMessage = customerService.addFeedback(token, feedbackDto);
        Response response = new Response(feedbackMessage, null, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/comments")
    public ResponseEntity<Response> getBookFeedback(@RequestParam("isbnNumber") String isbnNumber) {
        List<FeedbackDTO> allFeedback = customerService.getAllFeedback(isbnNumber);
        Response response = new Response("Feedback Fetched Successfully", allFeedback, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/customer/feedback")
    public ResponseEntity<Response> getCustomerFeedback(@RequestParam("bookId") Integer bookId, @RequestHeader String token) {
        List<FeedbackDTO> customerFeedback = customerService.getUserFeedback(bookId, token);
        Response response = new Response("User Feedback Fetched Successfully", customerFeedback, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
