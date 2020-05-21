package com.enigma.bookstore.controller;

import com.enigma.bookstore.dto.ResetPasswordDTO;
import com.enigma.bookstore.dto.Response;
import com.enigma.bookstore.dto.UserLoginDTO;
import com.enigma.bookstore.dto.UserRegistrationDTO;
import com.enigma.bookstore.exception.BookException;
import com.enigma.bookstore.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @PostMapping("/resend/email/{email}")
    public ResponseEntity<Response> sendEmailWithTokenLink(@PathVariable(name = "email") String email) {
        String message = userService.sendEmailWithTokenLink(email);
        Response response = new Response(message, null, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/verify/email/")
    public ResponseEntity<Response> verifyEmail(@RequestParam(value = "token", defaultValue = "") String token) {
        String message = userService.verifyEmail(token);
        Response response = new Response(message, null, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Response> login(@Valid @RequestBody UserLoginDTO userLoginDTO, HttpServletResponse httpServletResponse, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return new ResponseEntity(bindingResult.getAllErrors().get(0).getDefaultMessage(), HttpStatus.ALREADY_REPORTED);
        String token = userService.userLogin(userLoginDTO);
        httpServletResponse.setHeader("authorization", token);
        Response response = new Response("Login Successful", null, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/reset/password/")
    public ResponseEntity<Response> resetPassword(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO, @RequestParam(value = "token", defaultValue = "") String token, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new BookException("Invalid Data !!! Please Enter Valid Password");
        String message = userService.resetPassword(resetPasswordDTO, token);
        Response response = new Response(message, null, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}