package com.enigma.bookstore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "orderProducts")
public class OrderProducts {

    @Id
    @GeneratedValue(
            strategy= GenerationType.AUTO,
            generator="native"
    )
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
    private Integer OrderProductsId;
    private Integer quantity;

    @ManyToOne()
    @JoinColumn(name = "bookId")
    private Book book;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "orderId")
    private Orders orders;

    public OrderProducts(Book book, Orders orders, Integer quantity) {
        this.quantity = quantity;
        this.orders = orders;
        this.book = book;
    }
}