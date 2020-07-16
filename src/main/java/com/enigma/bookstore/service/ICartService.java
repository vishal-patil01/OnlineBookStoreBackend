package com.enigma.bookstore.service;

import com.enigma.bookstore.dto.CartDTO;
import com.enigma.bookstore.model.CartItems;
import com.enigma.bookstore.model.User;

import java.util.List;

public interface ICartService {
    String createCart(User user);

    String addToCart(CartDTO bookCartDTO, String token);

    List<CartItems> fetchCart(String token);

    String deleteBookFromCart(Integer cartItemId, String token);

    String updateCartItemQuantity(Integer cartItemId, Integer quantity, String token);
}
