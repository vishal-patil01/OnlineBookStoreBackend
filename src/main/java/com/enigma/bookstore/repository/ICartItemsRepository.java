package com.enigma.bookstore.repository;

import com.enigma.bookstore.model.CartItems;
import com.enigma.bookstore.model.WishListItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ICartItemsRepository extends JpaRepository<CartItems, Integer> {
    @Query(value = "select * from cartitems where cart_id = :cartId", nativeQuery = true)
    List<CartItems> findAllByCart_CardId(Integer cartId);

    @Transactional
    @Modifying
    @Query(value = "delete from cartitems where cart_id = :cartId", nativeQuery = true)
    Integer deleteCartItems(@Param("cartId") Integer cartId);

    @Query(value = "select * from cartitems where cart_id = :cartId and book_id = :bookId", nativeQuery = true)
    List<CartItems> findByBookIdAndCart_CardId(Integer bookId, Integer cartId);

    List<CartItems> findAllByBookId(Integer bookId);
}
