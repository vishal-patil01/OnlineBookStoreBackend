package com.enigma.bookstore.repository;

import com.enigma.bookstore.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IWishListRepository extends JpaRepository<WishList, Integer> {
    Optional<WishList> findByUserId(Integer id);
}
