package com.enigma.bookstore.controller;

import com.enigma.bookstore.dto.BookDTO;
import com.enigma.bookstore.dto.Response;
import com.enigma.bookstore.dto.UserLoginDTO;
import com.enigma.bookstore.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("bookstore/admin")
public class AdminController {

    @Autowired
    IAdminService adminService;

    @PostMapping("/book")
    public ResponseEntity<Response> addBook(@Valid @RequestBody BookDTO bookDTO, BindingResult bindingResult) {
        return getResponseResponseEntity(bindingResult, adminService.addBook(bookDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<Response> adminLogin(@Valid @RequestBody UserLoginDTO userLoginDTO, BindingResult bindingResult) {
        return getResponseResponseEntity(bindingResult, adminService.adminLogin(userLoginDTO));
    }

    @PostMapping("/image")
    public ResponseEntity<Response> uploadImage(@RequestParam("file") MultipartFile file) {
        String fileDownloadURL = adminService.uploadImage(file);
        Response response = new Response("Image Loaded Successfully", fileDownloadURL, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/book/{bookid}")
    public ResponseEntity<Response> updateBook(@Valid @RequestBody BookDTO bookDTO, @PathVariable(name = "bookid") Integer bookId, BindingResult bindingResult) {
        return getResponseResponseEntity(bindingResult, adminService.updateBook(bookDTO, bookId));
    }
    @DeleteMapping("/book/{bookid}")
    public ResponseEntity<Response> deleteBook(@PathVariable(name = "bookid") Integer bookId) {
        String message = adminService.deleteBook(bookId);
        Response response = new Response(message, null, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    private ResponseEntity<Response> getResponseResponseEntity(BindingResult bindingResult, String serviceResponse) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(bindingResult.getAllErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }
        String message = serviceResponse;
        Response response = new Response(message, null, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
