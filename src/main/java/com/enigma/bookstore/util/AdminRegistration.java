package com.enigma.bookstore.util;

import com.enigma.bookstore.dto.UserRegistrationDTO;
import com.enigma.bookstore.enums.UserRole;
import com.enigma.bookstore.model.User;
import com.enigma.bookstore.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AdminRegistration {

    @Autowired
    IUserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @EventListener
    public void appReady(ApplicationReadyEvent event) {
        Optional<User> admin = userRepository.findByEmail("bookstore.engima@gmail.com");
        if (!admin.isPresent()) {
            UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO("Admin", "bookstore.engima@gmail.com", "Enigma@123", "9876543210", true, UserRole.ADMIN);
            User user = new User(userRegistrationDTO);
            user.setPassword(bCryptPasswordEncoder.encode(userRegistrationDTO.password));
            userRepository.save(user);
        }
    }
}
