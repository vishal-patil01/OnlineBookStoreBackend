package com.enigma.bookstore.service;

import com.enigma.bookstore.dto.CustomerDTO;
import com.enigma.bookstore.dto.FeedbackDTO;
import com.enigma.bookstore.enums.AddressType;
import com.enigma.bookstore.model.Customer;

import java.util.List;

public interface ICustomerService {
    String addCustomerDetails(CustomerDTO customerDTO, String token);

    Customer fetchCustomerDetails(AddressType addressType, String token);

    String addFeedback(String token, FeedbackDTO feedbackDto);

    List<FeedbackDTO> getAllFeedback(String isbnNumber);

    List<FeedbackDTO> getUserFeedback(Integer id, String token);
}
