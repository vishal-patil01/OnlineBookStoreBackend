package com.enigma.bookstore.controller;

import com.enigma.bookstore.dto.Response;
import com.enigma.bookstore.model.WishListItems;
import com.enigma.bookstore.service.IWishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/wishlist")
    public ResponseEntity<Response> fetchWishList(@RequestHeader(value = "token", required = false) String token) {
        List<WishListItems> wishListItems = wishListService.fetchWishList(token);
        Response response = new Response("WishList Data Fetched Successfully", wishListItems, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/wishlist/{bookid}")
    public ResponseEntity<Response> deleteBookFromWishList(@PathVariable(name = "bookid") Integer bookId, @RequestHeader(value = "token", required = false) String token) {
        String message = wishListService.deleteBookFromWishList(bookId, token);
        Response response = new Response(message, null, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}