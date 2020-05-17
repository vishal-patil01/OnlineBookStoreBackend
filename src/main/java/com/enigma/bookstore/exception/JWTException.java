package com.enigma.bookstore.exception;

import lombok.Getter;

@Getter
public class JWTException extends RuntimeException {
    public JWTException(String message) {
        super(message);
    }
}
