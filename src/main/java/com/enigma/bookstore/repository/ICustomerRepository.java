package com.enigma.bookstore.repository;

import com.enigma.bookstore.enums.AddressType;
import com.enigma.bookstore.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICustomerRepository extends JpaRepository<Customer, Integer> {
    List<Customer> findByUserIdAndAndCustomerAddressType(Integer usrId, AddressType addressType);
}
