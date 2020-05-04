package com.enigma.bookstore.exception;

import lombok.Getter;

@Getter
public class AdminException extends RuntimeException {
    public ExceptionType type;

    public enum ExceptionType {BOOK_AND_AUTHOR_NAME_ALREADY_EXISTS, ISBN_NUMBER_ALREADY_EXISTS}

    public AdminException(String message, ExceptionType type) {
        super(message);
        this.type = type;
    }


}
