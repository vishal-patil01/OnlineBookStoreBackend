package com.enigma.bookstore.controller;

import com.enigma.bookstore.dto.CartDTO;
import com.enigma.bookstore.dto.Response;
import com.enigma.bookstore.exception.CartException;
import com.enigma.bookstore.model.CartItems;
import com.enigma.bookstore.service.ICartService;
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
public class CartController {

    @Autowired
    ICartService bookCartService;

    @PostMapping("/cart")
    public ResponseEntity<Response> addToCart(@RequestHeader(value = "token", required = false) String token,@Valid @RequestBody CartDTO cartDTO,  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(bindingResult.getAllErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }
        String message = bookCartService.addToCart(cartDTO, token);
        Response response = new Response(message, null, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/cart")
    public ResponseEntity<Response> fetchCartBook(@RequestHeader(value = "token", required = false) String token) {
        List<CartItems> cartData = bookCartService.fetchCart(token);
        Response response = new Response("Cart Data Fetched Successfully", cartData, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/cart/{cartitemid}")
    public ResponseEntity<Response> deleteBookFromCart(@PathVariable(name = "cartitemid") Integer cartItemId, @RequestHeader(value = "token", required = false) String token) {
        String message = bookCartService.deleteBookFromCart(cartItemId, token);
        Response response = new Response(message, null, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/cart/{cartitemid}/{quantity}")
    public ResponseEntity<Response> updateCartBookQuantity(@PathVariable(name = "cartitemid") Integer cartItemId, @PathVariable Integer quantity, @RequestHeader(value = "token", required = false) String token) {
        String message = bookCartService.updateCartItemQuantity(cartItemId, quantity, token);
        Response response = new Response(message, null, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}