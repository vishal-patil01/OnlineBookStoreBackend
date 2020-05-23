package com.enigma.bookstore.controller;

import com.enigma.bookstore.dto.CartDTO;
import com.enigma.bookstore.dto.Response;
import com.enigma.bookstore.exception.CartException;
import com.enigma.bookstore.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/bookstore")
public class CartController {

    @Autowired
    ICartService bookCartService;

    @PostMapping("/cart")
    public ResponseEntity<Response> addToCart(@RequestBody CartDTO bookCartDTO, @RequestHeader(value = "token", required = false) String token, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new CartException("Invalid Data !!! Please Enter Valid Data");
        String message = bookCartService.addToCart(bookCartDTO, token);
        Response response = new Response(message, null, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}