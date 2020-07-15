package com.enigma.bookstore.service;

import com.enigma.bookstore.dto.OrderDTO;
import com.enigma.bookstore.model.Orders;

import java.util.List;

public interface IOrderService {
    String placeOrder(OrderDTO orderDTO, String token);

    List<Orders> fetchOrders(String token);
}
