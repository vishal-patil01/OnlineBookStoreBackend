package com.enigma.bookstore.exception;

import lombok.Getter;

@Getter
public class UserException extends RuntimeException {
    public UserException(String message) {
        super(message);
    }
}
