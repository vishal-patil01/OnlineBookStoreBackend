package com.enigma.bookstore.util;

import com.enigma.bookstore.model.Book;

import java.util.List;

public interface IMailService {
    void sendEmail(String email, String subject, String message);
    void sendEmail(String email, String subject, String message, List<Book> attachments);
}
