package com.enigma.bookstore.repository;

import com.enigma.bookstore.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByUserId(Integer id);
}
