package com.enigma.bookstore.service.implementation;

import com.enigma.bookstore.dto.CartDTO;
import com.enigma.bookstore.service.ICartService;
import org.springframework.stereotype.Service;

@Service
public class CartService implements ICartService {

    @Override
    public String addToCart(CartDTO bookCartDTO, String token) {
        return null;
    }
}


