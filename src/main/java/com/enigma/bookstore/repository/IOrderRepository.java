package com.enigma.bookstore.repository;

import com.enigma.bookstore.model.Book;
import com.enigma.bookstore.model.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IOrderRepository extends JpaRepository<Orders, Integer> {
    List<Orders> findOrdersByUser_IdOrderByOrderPlacedDateDesc(Integer userId);

    @Query(value = "select * from orders", nativeQuery = true)
    Page<Orders> fetchOrders(Pageable pageable);
}
