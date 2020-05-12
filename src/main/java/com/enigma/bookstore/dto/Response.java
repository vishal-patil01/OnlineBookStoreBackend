package com.enigma.bookstore.dto;

public class Response {
    public Integer statusCode;
    public String message;
    public Object data;

    public Response(String message, Object data, Integer statusCode) {
        this.message = message;
        this.data = data;
        this.statusCode = statusCode;
    }
}

