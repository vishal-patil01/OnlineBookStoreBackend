package com.enigma.bookstore.service;

import com.enigma.bookstore.dto.CartDTO;

public interface ICartService {
    String addToCart(CartDTO bookCartDTO, String token);
}
