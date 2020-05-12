package com.enigma.bookstore.exception;

import lombok.Getter;

@Getter
public class BookException extends RuntimeException {
    public BookException(String message) {
        super(message);
    }
}
