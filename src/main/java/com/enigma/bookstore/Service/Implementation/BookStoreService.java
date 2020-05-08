package com.enigma.bookstore.service.implementation;

import com.enigma.bookstore.dto.Response;
import com.enigma.bookstore.repository.IBookStoreRepository;
import com.enigma.bookstore.service.IBookStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookStoreService implements IBookStoreService {

    @Autowired
    IBookStoreRepository iBookStoreRepository;

    @Override
    public Response getAllBooks(int pageStart, int pageEnd) {
        return null;
    }

    @Override
    public int getTotalBookCount() {
        return 0;
    }
}
