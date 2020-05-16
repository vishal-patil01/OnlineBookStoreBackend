package com.enigma.bookstore.service;

import com.enigma.bookstore.dto.UserRegistrationDTO;

public interface IUserService {
    String userRegistration(UserRegistrationDTO userRegistrationDTO);
}
