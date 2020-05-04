package com.enigma.bookstore.exception;

import com.enigma.bookstore.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
@ControllerAdvice
public class AdminExceptionHandler {
    @ExceptionHandler(AdminException.class)
    public ResponseEntity<Response> onlineAdminExceptionHandler(AdminException e) {
        Response response = new Response(208, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.ALREADY_REPORTED);
    }
}
