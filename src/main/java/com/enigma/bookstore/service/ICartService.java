package com.enigma.bookstore.service;

import com.enigma.bookstore.dto.CartDTO;
import com.enigma.bookstore.model.User;

public interface ICartService {
    void createCart(User user);

    String addToCart(CartDTO bookCartDTO, String token);
}
