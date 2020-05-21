package com.enigma.bookstore.service;

import com.enigma.bookstore.dto.ResetPasswordDTO;
import com.enigma.bookstore.dto.UserLoginDTO;
import com.enigma.bookstore.dto.UserRegistrationDTO;

import javax.servlet.http.HttpServletRequest;

public interface IUserService {
    String userRegistration(UserRegistrationDTO userRegistrationDTO, HttpServletRequest httpServletRequest);
    String sendEmailWithTokenLink(String email, HttpServletRequest httpServletRequest);
    String verifyEmail(String token);
    String userLogin(UserLoginDTO userLoginDTO);
    String resetPassword(ResetPasswordDTO pass, String token);
}
