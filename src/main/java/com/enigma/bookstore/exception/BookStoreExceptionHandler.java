package com.enigma.bookstore.exception;

import com.enigma.bookstore.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BookStoreExceptionHandler {
    @ExceptionHandler(BookException.class)
    public ResponseEntity<Response> onlineBookStoreExceptionHandler(BookException e) {
        Response response = new Response(e.getMessage(), null, 208);
        return new ResponseEntity<>(response, HttpStatus.ALREADY_REPORTED);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<Response> onlineBookStoreExceptionHandler(UserException e) {
        Response response = new Response(e.getMessage(), null, 208);
        return new ResponseEntity<>(response, HttpStatus.ALREADY_REPORTED);
    }

    @ExceptionHandler(JWTException.class)
    public ResponseEntity<Response> onlineBookStoreExceptionHandler(JWTException e) {
        Response response = new Response(e.getMessage(), null, 208);
        return new ResponseEntity<>(response, HttpStatus.ALREADY_REPORTED);
    }
}
