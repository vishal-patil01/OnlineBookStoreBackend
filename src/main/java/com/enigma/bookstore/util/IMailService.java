package com.enigma.bookstore.util;

public interface IMailService {
    String sendEmail(String email, String subject, String message);
}
