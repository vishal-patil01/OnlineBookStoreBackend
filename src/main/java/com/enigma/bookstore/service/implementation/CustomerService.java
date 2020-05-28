package com.enigma.bookstore.service.implementation;

import com.enigma.bookstore.dto.CustomerDTO;
import com.enigma.bookstore.enums.AddressType;
import com.enigma.bookstore.exception.CustomerException;
import com.enigma.bookstore.exception.UserException;
import com.enigma.bookstore.model.Customer;
import com.enigma.bookstore.model.User;
import com.enigma.bookstore.repository.ICustomerRepository;
import com.enigma.bookstore.repository.IUserRepository;
import com.enigma.bookstore.service.ICustomerService;
import com.enigma.bookstore.util.IJWTToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService implements ICustomerService {

    @Autowired
    private ICustomerRepository customerRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IJWTToken jwtToken;

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
        customer.setCustomerAddress(customerDTO.customerAddress);
        customer.setCustomerLandmark(customerDTO.customerLandmark);
        customer.setCustomerLocality(customerDTO.customerLocality);
        customer.setCustomerPinCode(customerDTO.customerPinCode);
        customer.setCustomerTown(customerDTO.customerTown);
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
}
