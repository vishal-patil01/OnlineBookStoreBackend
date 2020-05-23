package com.enigma.bookstore.dto;

public class CartDTO {
    public Integer id;
    public Integer quantity;

    public CartDTO(Integer bookId, Integer quantity) {
        this.id = bookId;
        this.quantity = quantity;
    }
}