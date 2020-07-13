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
@CrossOrigin(origins = "*", exposedHeaders = "*,")
@RequestMapping("bookstore/admin")
public class AdminController {

    @Autowired
    IAdminService adminService;

    @PostMapping("/book")
    public ResponseEntity<Response> addBook(@Valid @RequestBody BookDTO bookDTO, @RequestHeader(value = "token", required = false) String token, BindingResult bindingResult) {
        return getResponseResponseEntity(bindingResult, adminService.addBook(bookDTO, token));
    }

    @PostMapping("/login")
    public ResponseEntity<Response> adminLogin(@Valid @RequestBody UserLoginDTO userLoginDTO, HttpServletResponse httpServletResponse, BindingResult bindingResult) {
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
    public ResponseEntity<Response> updateBook(@Valid @RequestBody BookDTO bookDTO, @PathVariable(name = "bookid") Integer bookId, @RequestHeader(value = "token", required = false) String token, BindingResult bindingResult) {
        return getResponseResponseEntity(bindingResult, adminService.updateBook(bookDTO, bookId, token));
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
