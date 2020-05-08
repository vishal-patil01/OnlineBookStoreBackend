package com.enigma.bookstore.dto;

import com.enigma.bookstore.model.Book;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Response {
    private Integer statusCode;
    private String message;
    List<Book> bookList;

    public Response(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    public Response(List<Book> bookList, int statusCode) {
        this.statusCode = statusCode;
        this.bookList = bookList;
    }
}
