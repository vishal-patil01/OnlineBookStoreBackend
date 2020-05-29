package com.enigma.bookstore.repository;

import com.enigma.bookstore.model.OrderProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderProductsRepository extends JpaRepository<OrderProducts, Integer> {
}
