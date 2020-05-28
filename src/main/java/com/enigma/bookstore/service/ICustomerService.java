package com.enigma.bookstore.service;

import com.enigma.bookstore.dto.CustomerDTO;
import com.enigma.bookstore.enums.AddressType;
import com.enigma.bookstore.model.Customer;

public interface ICustomerService {

    String addCustomerDetails(CustomerDTO customerDTO, String token);

    Customer fetchCustomerDetails(AddressType addressType, String token);

}
