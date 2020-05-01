package com.enigma.bookstore.Controller;

import com.enigma.bookstore.Service.IAdminService;
import com.enigma.bookstore.dao.BookDAO;
import com.enigma.bookstore.model.BookDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookstore/admin")
public class AdminController {

    @Autowired
    IAdminService adminService;

    @PostMapping("/addbook")
    public ResponseEntity<BookDetails> addBooks(@RequestBody BookDAO bookDAO) {
        BookDetails booksAdded = adminService.addBook(bookDAO);
        return new ResponseEntity<>(booksAdded, HttpStatus.OK);
    }
}
