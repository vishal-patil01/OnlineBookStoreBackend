package com.enigma.bookstore.service;

import com.enigma.bookstore.dto.BookDTO;
import com.enigma.bookstore.dto.Response;

public interface IAdminService {
    Response addBook(BookDTO bookDTO);
}
