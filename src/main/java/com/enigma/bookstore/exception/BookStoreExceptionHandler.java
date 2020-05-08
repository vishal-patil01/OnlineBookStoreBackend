package com.enigma.bookstore.exception;

import com.enigma.bookstore.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BookStoreExceptionHandler {
    @ExceptionHandler(BookStoreException.class)
    public ResponseEntity<Response> onlineBookStoreExceptionHandler(BookStoreException e) {
        Response response = new Response(e.getMessage(), 208);
        return new ResponseEntity<>(response, HttpStatus.ALREADY_REPORTED);
    }
}
