package com.enigma.bookstore.exception;

import lombok.Getter;

@Getter
public class OrderException extends RuntimeException {
    public OrderException(String message) {
        super(message);
    }
}
