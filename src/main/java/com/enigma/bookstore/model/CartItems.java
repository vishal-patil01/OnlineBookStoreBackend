package com.enigma.bookstore.model;

import com.enigma.bookstore.dto.CartDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "cartitems")
public class CartItems {

    @Id
    @GeneratedValue(
            strategy= GenerationType.AUTO,
            generator="native"
    )
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
    private Integer cartItemsId;
    private Integer quantity;
    @CreationTimestamp
    private Timestamp addedToCartDate;

    @ManyToOne()
    @JoinColumn(name = "bookId")
    private Book book;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "cartId")
    private Cart cart;

    public CartItems(CartDTO cartDTO, Book book, Cart cart) {
        this.quantity = cartDTO.quantity;
        this.book = book;
        this.cart = cart;
    }
}