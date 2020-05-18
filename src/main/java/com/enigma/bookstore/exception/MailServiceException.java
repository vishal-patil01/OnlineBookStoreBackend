package com.enigma.bookstore.exception;

import lombok.Getter;

@Getter
public class MailServiceException extends RuntimeException {
    public MailServiceException(String message) {
        super(message);
    }
}
