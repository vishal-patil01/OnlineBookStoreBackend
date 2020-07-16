package com.enigma.bookstore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "wishlistitems")
public class WishListItems implements Serializable {

    @Id
    @GeneratedValue(
            strategy= GenerationType.AUTO,
            generator="native"
    )
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
    private Integer wishListItemsId;

    @CreationTimestamp
    private Timestamp addedToCartDate;

    @ManyToOne()
    @JoinColumn(name = "bookId")
    private Book book;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "wishListId")
    private WishList wishList;

    public WishListItems(Book book, WishList wishList) {
        this.book = book;
        this.wishList = wishList;
    }
}