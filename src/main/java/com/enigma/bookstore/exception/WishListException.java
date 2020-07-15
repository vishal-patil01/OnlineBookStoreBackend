package com.enigma.bookstore.exception;

import lombok.Getter;

@Getter
public class WishListException extends RuntimeException {
    public WishListException(String message) {
        super(message);
    }
}

