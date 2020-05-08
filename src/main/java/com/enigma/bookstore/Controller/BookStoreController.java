package com.enigma.bookstore.controller;


import com.enigma.bookstore.dto.Response;
import com.enigma.bookstore.service.IBookStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/bookstore")
public class BookStoreController {

    @Autowired
    private IBookStoreService iBookStoreService;

    @GetMapping("/book/{pageStart}/{pageSize}")
    public ResponseEntity<Response> getLimitedBooks(@PathVariable int pageStart, @PathVariable int pageSize) {
        return null;
    }

    @GetMapping("/count")
    public int getTotalBookCount() {
        return 0;
    }
}
