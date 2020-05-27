package com.enigma.bookstore.service.implementation;

import com.enigma.bookstore.dto.CustomerDTO;
import com.enigma.bookstore.service.ICustomerService;
import org.springframework.stereotype.Service;

@Service
public class CustomerService implements ICustomerService {

    @Override
    public String addCustomerDetails(CustomerDTO customerDTO, String token) {
        return null;
    }
}
