package com.enigma.bookstore.repository;

import com.enigma.bookstore.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IOrderRepository extends JpaRepository<Orders, Integer> {
    List<Orders> findOrdersByUser_IdOrderByOrderPlacedDateDesc(Integer userId);

    Optional<Orders> findByOrderId(Integer orderId);
}
