package com.enigma.bookstore.controller;

import com.enigma.bookstore.dto.Response;
import com.enigma.bookstore.service.IWishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/bookstore/")
public class WishListController {

    @Autowired
    IWishListService wishListService;

    @PostMapping("/wishlist/{bookid}")
    public ResponseEntity<Response> addToWishList(@PathVariable(name = "bookid") Integer bookId, @RequestHeader(value = "token", required = false) String token) {
        String message = wishListService.addToWishList(bookId, token);
        Response response = new Response(message, null, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}