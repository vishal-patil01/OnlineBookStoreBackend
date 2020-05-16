package com.enigma.bookstore.controller;

import com.enigma.bookstore.dto.Response;
import com.enigma.bookstore.dto.UserRegistrationDTO;
import com.enigma.bookstore.exception.BookException;
import com.enigma.bookstore.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@CrossOrigin(origins = "*", exposedHeaders = "*,")
@RestController
@RequestMapping("bookstore/user")
public class UserController {

    @Autowired
    IUserService userService;

    @Autowired
    HttpServletRequest httpServletRequest;

    @PostMapping("/register")
    public ResponseEntity<Response> register(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new BookException("Invalid Data !!! Please Enter Correct Data");
        String message = userService.userRegistration(userRegistrationDTO);
        Response response = new Response(message, null, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}