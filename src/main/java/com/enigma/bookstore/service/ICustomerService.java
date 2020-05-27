package com.enigma.bookstore.service;

import com.enigma.bookstore.dto.CustomerDTO;

public interface ICustomerService {

    String addCustomerDetails(CustomerDTO customerDTO, String token);
}
