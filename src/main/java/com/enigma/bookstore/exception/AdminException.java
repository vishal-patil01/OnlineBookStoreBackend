package com.enigma.bookstore.exception;

import lombok.Getter;

@Getter
public class AdminException extends RuntimeException {
    public ExceptionType type;

    public enum ExceptionType {ISBN_NUMBER_ALREADY_EXISTS}

    public AdminException(String message, ExceptionType type) {
        super(message);
        this.type = type;
    }
}
