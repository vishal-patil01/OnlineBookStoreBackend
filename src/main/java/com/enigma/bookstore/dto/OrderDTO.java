package com.enigma.bookstore.dto;

import com.enigma.bookstore.enums.AddressType;

public class OrderDTO {
    public AddressType addressType;
    public Double totalPrice;

    public OrderDTO(AddressType addressType, Double totalPrice) {
        this.addressType = addressType;
        this.totalPrice = totalPrice;
    }
}