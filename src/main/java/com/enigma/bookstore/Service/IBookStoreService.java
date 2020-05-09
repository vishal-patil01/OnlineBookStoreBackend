package com.enigma.bookstore.service;

import com.enigma.bookstore.dto.Response;

public interface IBookStoreService {
    Response getAllBooks(int pageStart, int pageEnd);

    int getTotalBookCount();
}
