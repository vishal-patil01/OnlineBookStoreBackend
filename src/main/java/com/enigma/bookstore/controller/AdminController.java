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

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*", exposedHeaders = "authorization")
@RequestMapping("bookstore/admin")
public class AdminController {

    @Autowired
    IAdminService adminService;

    @PostMapping("/book")
    public ResponseEntity<Response> addBook(@RequestHeader(value = "token", required = false) String token,@Valid @RequestBody BookDTO bookDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(bindingResult.getAllErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }
        String message = adminService.addBook(bookDTO, token);
        Response response = new Response(message, null, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Response> adminLogin( HttpServletResponse httpServletResponse,@Valid @RequestBody UserLoginDTO userLoginDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(bindingResult.getAllErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }
        String token = adminService.adminLogin(userLoginDTO);
        httpServletResponse.setHeader("authorization", token);
        Response response = new Response("Login Successful", "Admin", 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/image")
    public ResponseEntity<Response> uploadImage(@RequestParam("file") MultipartFile file, @RequestHeader(value = "token", required = false) String token) {
        String fileDownloadURL = adminService.uploadImage(file, token);
        Response response = new Response("Image UpLoaded Successfully", fileDownloadURL, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/book/{bookid}")
    public ResponseEntity<Response> updateBook( @RequestHeader(value = "token", required = false) String token,@Valid @RequestBody BookDTO bookDTO, @PathVariable(name = "bookid") Integer bookId, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(bindingResult.getAllErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }
        String message = adminService.updateBook(bookDTO, bookId, token);
        Response response = new Response(message, null, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
