package com.enigma.bookstore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Double totalPrice;
    @Column(unique = true)
    private Integer orderId;
    @CreationTimestamp
    private Timestamp orderPlacedDate;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "userId")
    private User user;


    @OneToMany(mappedBy = "orders")
    private List<OrderProducts> orderProducts;

    @ManyToOne()
    @JoinColumn(name = "customer")
    private Customer customer;

    public Orders(User user, Double totalPrice, Customer customerDetails, Integer orderId){
        this.user = user;
        this.customer = customerDetails;
        this.totalPrice = totalPrice;
        this.orderId=orderId;
    }
}