package com.enigma.bookstore.exception;

import lombok.Getter;

@Getter
public class CustomerException extends RuntimeException {
    public CustomerException(String message) {
        super(message);
    }
}
