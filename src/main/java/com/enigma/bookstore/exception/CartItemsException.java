package com.enigma.bookstore.exception;

import lombok.Getter;

@Getter
public class CartItemsException extends RuntimeException {
    public CartItemsException(String message) {
        super(message);
    }
}
