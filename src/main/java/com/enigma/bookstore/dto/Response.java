package com.enigma.bookstore.dto;

import com.enigma.bookstore.model.Book;

import java.util.List;

public class Response {
    private int statusCode;
    private String message;
    List<BookDTO> bookList;

    public String getMessage() {
        return message;
    }

    public List<BookDTO> getBookList() {
        return bookList;
    }

    public Response(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    public Response(List<BookDTO> bookList, int statusCode) {
        this.statusCode = statusCode;
        this.bookList = bookList;
    }
}
