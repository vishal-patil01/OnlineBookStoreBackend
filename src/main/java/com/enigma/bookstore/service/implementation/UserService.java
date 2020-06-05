package com.enigma.bookstore.service.implementation;

import com.enigma.bookstore.dto.ResetPasswordDTO;
import com.enigma.bookstore.dto.UserLoginDTO;
import com.enigma.bookstore.dto.UserRegistrationDTO;
import com.enigma.bookstore.exception.UserException;
import com.enigma.bookstore.model.User;
import com.enigma.bookstore.repository.IUserRepository;
import com.enigma.bookstore.service.ICartService;
import com.enigma.bookstore.service.IUserService;
import com.enigma.bookstore.service.IWishListService;
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
    public EmailTemplateGenerator emailTemplateGenerator;

    @Autowired
    ICartService cartService;

    @Autowired
    IWishListService wishListService;

    @Override
    public String userRegistration(UserRegistrationDTO userRegistrationDTO, HttpServletRequest httpServletRequest) {
        boolean isUserPresent = userRepository.findByEmail(userRegistrationDTO.email).isPresent();
        if (isUserPresent)
            throw new UserException("User With This Email Address Already Exists");
        String password = bCryptPasswordEncoder.encode(userRegistrationDTO.password);
        User user = new User(userRegistrationDTO);
        user.setPassword(password);
        userRepository.save(user);
        String token = jwtToken.generateToken(user.getId(), getExpirationTime(Calendar.MINUTE, 10));
        String message = emailTemplateGenerator.getHeader(user.getFullName()) + getURL(token, httpServletRequest) + emailTemplateGenerator.getFooter();
        mailService.sendEmail(user.getEmail(), getEmailSubject(httpServletRequest), message);
        return "Registration Successful";
    }

    @Override
    public String userLogin(UserLoginDTO userLoginDTO) {
        User user = getUser(userLoginDTO.email);
        if (user.isEmailVerified()) {
            boolean isPasswordMatched = bCryptPasswordEncoder.matches(userLoginDTO.password, user.getPassword());
            if (isPasswordMatched) {
                Date expirationTime = getExpirationTime(Calendar.DAY_OF_YEAR, 1);
                return jwtToken.generateToken(user.getId(), expirationTime);
            }
            throw new UserException("Enter Valid Password");
        }
        throw new UserException("First Verify Your Email Address");
    }

    @Override
    public String sendEmailWithTokenLink(String email, HttpServletRequest httpServletRequest) {
        User user = getUser(email);
        Date expirationTime = getExpirationTime(Calendar.MINUTE, 10);
        String generateToken = jwtToken.generateToken(user.getId(), expirationTime);
        String url = emailTemplateGenerator.getHeader(user.getFullName()) + getURL(generateToken, httpServletRequest) + emailTemplateGenerator.getFooter();
        String emailSubject = getEmailSubject(httpServletRequest);
        mailService.sendEmail(email, emailSubject, url);
        return "Verification Email Has Been Sent";
    }

    @Override
    public String verifyEmail(String token) {
        int userId = jwtToken.verifyToken(token);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserException("User Not Found"));
        if (user.isEmailVerified())
            throw new UserException("Email Already Verified");
        user.setEmailVerified(true);
        userRepository.save(user);
        cartService.createCart(user);
        wishListService.createWishList(user);
        return "Email Address Verified";
    }

    @Override
    public String resetPassword(ResetPasswordDTO resetPasswordDTO, String token) {
        int userId = jwtToken.verifyToken(token);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserException("User Not Found"));
        String password = bCryptPasswordEncoder.encode(resetPasswordDTO.password);
        user.setPassword(password);
        userRepository.save(user);
        return "Password Reset Successfully";
    }

    @Override
    public String getUserFullName(String email) {
        return userRepository.findByEmail(email).get().getFullName();
    }

    @Override
    public User fetchUserDetails(String token) {
        int userId = this.jwtToken.verifyToken(token);
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException("UseId Not Exist"));
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

    private String getURL(String generateToken, HttpServletRequest httpServletRequest) {
        if (httpServletRequest.getHeader("Referer").contains("forget"))
            return emailTemplateGenerator.getResetPasswordTemplate(generateToken);
        return emailTemplateGenerator.getVerifyEmailTemplate(generateToken);
    }

    private String getEmailSubject(HttpServletRequest httpServletRequest) {
        if (httpServletRequest.getHeader("Referer").contains("forget"))
            return "Reset Password";
        return "Verify Your Email";
    }
}