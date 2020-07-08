package com.enigma.bookstore.service.implementation;

import com.enigma.bookstore.dto.CustomerDTO;
import com.enigma.bookstore.dto.FeedbackDTO;
import com.enigma.bookstore.enums.AddressType;
import com.enigma.bookstore.exception.CustomerException;
import com.enigma.bookstore.exception.UserException;
import com.enigma.bookstore.model.Book;
import com.enigma.bookstore.model.Customer;
import com.enigma.bookstore.model.Feedback;
import com.enigma.bookstore.model.User;
import com.enigma.bookstore.repository.IBookRepository;
import com.enigma.bookstore.repository.ICustomerRepository;
import com.enigma.bookstore.repository.IUserRepository;
import com.enigma.bookstore.service.ICustomerService;
import com.enigma.bookstore.util.ITokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService implements ICustomerService {

    @Autowired
    private ICustomerRepository customerRepository;
    @Autowired
    private IBookRepository bookRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private ITokenGenerator jwtToken;

    @Override
    public String addCustomerDetails(CustomerDTO customerDTO, String token) {
        int userId = jwtToken.verifyToken(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException("User Not Exist"));
        Customer customerDetails = new Customer(customerDTO, user);
        List<Customer> customersList = customerRepository.findByUserIdAndAndCustomerAddressType(userId, customerDTO.customerAddressType);
        if (!customersList.isEmpty())
            return updateCustomerDetails(customerDTO, customersList.get(0));
        customerRepository.save(customerDetails);
        return "Customer Details Saved Successfully";
    }

    private String updateCustomerDetails(CustomerDTO customerDTO, Customer customer) {
        customer.updateCustomer(customerDTO);
        customerRepository.save(customer);
        return "Customer Details Updated Successfully";
    }

    @Override
    public Customer fetchCustomerDetails(AddressType addressType, String token) {
        int userId = jwtToken.verifyToken(token);
        List<Customer> customerList = customerRepository.findByUserIdAndAndCustomerAddressType(userId, addressType);
        if (customerList.isEmpty())
            throw new CustomerException("There is No CustomerData Available");
        return customerList.get(0);
    }

    @Override
    public String addFeedback(String token, FeedbackDTO feedbackDto) {
        return null;
    }
}
