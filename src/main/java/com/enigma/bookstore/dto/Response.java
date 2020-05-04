package com.enigma.bookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class Response {
    private Integer statusCode;
    private String message;

}
