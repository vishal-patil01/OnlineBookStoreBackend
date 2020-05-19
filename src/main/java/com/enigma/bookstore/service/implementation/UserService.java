package com.enigma.bookstore.service.implementation;

import com.enigma.bookstore.dto.UserRegistrationDTO;
import com.enigma.bookstore.exception.UserException;
import com.enigma.bookstore.model.User;
import com.enigma.bookstore.repository.IUserRepository;
import com.enigma.bookstore.service.IUserService;
import com.enigma.bookstore.util.EmailTemplateGenerator;
import com.enigma.bookstore.util.IMailService;
import com.enigma.bookstore.util.implementation.JWTToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;

@Service
public class UserService implements IUserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private JWTToken jwtToken;

    @Autowired
    IMailService mailService;

    @Autowired
    public HttpServletRequest httpServletRequest;

    @Autowired
    public EmailTemplateGenerator emailTemplateGenerator;

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

    @Override
    public String sendEmailWithTokenLink(String email) {
        User user = getUser(email);
        Date expirationTime = getExpirationTime(Calendar.MINUTE, 10);
        String generateToken = jwtToken.generateToken(user.getId(), expirationTime);
        String url = emailTemplateGenerator.getHeader(user.getFullName()) + getURL(generateToken) + emailTemplateGenerator.getFooter();
        String emailSubject = getEmailSubject();
        return mailService.sendEmail(email, emailSubject, url);
    }

    @Override
    public String verifyEmail(String token) {
        int userId = jwtToken.verifyToken(token);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserException("User Not Found"));
        if (user.isEmailVerified())
            throw new UserException("Email Already Verified");
        user.setEmailVerified(true);
        userRepository.save(user);
        return "Email Address Verified";
    }

    private Date getExpirationTime(Integer timePeriod, Integer value) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(timePeriod, value);
        return calendar.getTime();
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("Account With This Email Address Not Exist"));
    }

    private String getURL(String generateToken) {
        return emailTemplateGenerator.getVerifyEmailTemplate(generateToken);
    }

    private String getEmailSubject() {
        return "Verify Your Email";
    }
}