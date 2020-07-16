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
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "orders")
public class Orders implements Serializable {

    @Id
    @GeneratedValue(
            strategy= GenerationType.AUTO,
            generator="native"
    )
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
    private Integer id;
    private Double totalPrice;
    @Column(unique = true)
    private String orderId;
    @CreationTimestamp
    private Timestamp orderPlacedDate;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "userId")
    private User user;


    @OneToMany(mappedBy = "orders",fetch = FetchType.EAGER)
    private List<OrderProducts> orderProducts;

    @ManyToOne()
    @JoinColumn(name = "customer")
    private Customer customer;

    public Orders(User user, Double totalPrice, Customer customerDetails, String orderId){
        this.user = user;
        this.customer = customerDetails;
        this.totalPrice = totalPrice;
        this.orderId=orderId;
    }
}