package com.enigma.bookstore.dto;

import javax.validation.constraints.Min;

public class CartDTO {
    public Integer id;

    @Min(value=1,message="Quantity Must Be Greater Than 0")
    public Integer quantity;

    public CartDTO(Integer bookId, Integer quantity) {
        this.id = bookId;
        this.quantity = quantity;
    }
}