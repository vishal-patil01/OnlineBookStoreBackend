package com.enigma.bookstore.util;

import com.enigma.bookstore.model.CartItems;

import java.util.List;

public interface IMailService {
    String sendEmail(String email, String subject, String message, List<CartItems>... attachments);
}
