package com.enigma.bookstore.service.implementation;

import com.enigma.bookstore.service.IOrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderService implements IOrderService {

    @Override
    public Integer placeOrder(Double totalPrice, String token) {
        return null;
    }
}
