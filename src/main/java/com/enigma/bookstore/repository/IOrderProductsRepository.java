package com.enigma.bookstore.repository;

import com.enigma.bookstore.model.Book;
import com.enigma.bookstore.model.CartItems;
import com.enigma.bookstore.model.OrderProducts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IOrderProductsRepository extends JpaRepository<OrderProducts, Integer> {
}
