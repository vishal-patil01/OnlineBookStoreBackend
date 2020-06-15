package com.enigma.bookstore.model;

import com.enigma.bookstore.dto.CustomerDTO;
import com.enigma.bookstore.enums.AddressType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "native"
    )
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
    private Integer customerId;

    @NotNull
    @Range(min = 100000, message = "Invalid Pin Code")
    public Integer customerPinCode;

    @NotNull
    @Pattern(regexp = "^[A-Za-z]+[ ]*[A-Za-z]{2,}$")
    public String customerLocality;

    @NotNull
    @Pattern(regexp = "^.{3,50}$")
    public String customerAddress;

    @NotNull
    @Pattern(regexp = "^[A-Za-z]+[ ]*[A-Za-z]{2,}$")
    public String customerTown;

    @NotNull
    @Pattern(regexp = "^.{3,50}$")
    public String customerLandmark;

    @NotNull
    public AddressType customerAddressType;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public Customer(CustomerDTO customerDTO, User user) {
        this.customerPinCode = customerDTO.customerPinCode;
        this.customerLocality = customerDTO.customerLocality;
        this.customerAddress = customerDTO.customerAddress;
        this.customerTown = customerDTO.customerTown;
        this.customerLandmark = customerDTO.customerLandmark;
        this.customerAddressType = customerDTO.customerAddressType;
        this.user = user;
    }

    public void updateCustomer(CustomerDTO customerDTO) {
        this.customerPinCode = customerDTO.customerPinCode;
        this.customerLocality = customerDTO.customerLocality;
        this.customerAddress = customerDTO.customerAddress;
        this.customerTown = customerDTO.customerTown;
        this.customerLandmark = customerDTO.customerLandmark;
    }
}