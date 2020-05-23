package com.enigma.bookstore.exception;

import lombok.Getter;

@Getter
public class CartException extends RuntimeException {
    public CartException(String message) {
        super(message);
    }
}
