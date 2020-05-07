package com.enigma.bookstore.exception;

import lombok.Getter;

@Getter
public class AdminException extends RuntimeException {

    public AdminException(String message) {
        super(message);
    }
}
