package com.enigma.bookstore.dto;

import com.enigma.bookstore.enums.AddressType;
import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
public class CustomerDTO {

    @NotNull
    @Range(min = 100000, message = "Invalid Pin Code")
    public Integer customerPinCode;

    @NotNull
    @Pattern(regexp = "^[A-Za-z .]{3,}")
    public String customerLocality;

    @NotNull
    @Pattern(regexp = "^.{3,50}$")
    public String customerAddress;

    @NotNull
    @Pattern(regexp = "^[A-Za-z .]{3,}")
    public String customerTown;

    @NotNull
    @Pattern(regexp = "^[A-Za-z .]{3,}")
    public String customerLandmark;

    @NotNull
    public AddressType customerAddressType;
}