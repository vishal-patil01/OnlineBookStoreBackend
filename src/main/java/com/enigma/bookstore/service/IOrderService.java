package com.enigma.bookstore.service;

public interface IOrderService {
    Integer placeOrder(Double totalPrice, String token);
}
