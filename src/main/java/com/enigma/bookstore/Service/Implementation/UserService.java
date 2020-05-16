package com.enigma.bookstore.service.implementation;

import com.enigma.bookstore.dto.UserRegistrationDTO;
import com.enigma.bookstore.exception.UserException;
import com.enigma.bookstore.model.User;
import com.enigma.bookstore.repository.IUserRepository;
import com.enigma.bookstore.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserService implements IUserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private IUserRepository userRepository;

    @Override
    public String userRegistration(UserRegistrationDTO userRegistrationDTO) {
        boolean isUserPresent = userRepository.findByEmail(userRegistrationDTO.email).isPresent();
        if (isUserPresent)
            throw new UserException("User With This Email Address Already Exists");
        String password = bCryptPasswordEncoder.encode(userRegistrationDTO.password);
        User user = new User(userRegistrationDTO);
        user.setPassword(password);
        userRepository.save(user);
        return "Registration Successful";
    }
}