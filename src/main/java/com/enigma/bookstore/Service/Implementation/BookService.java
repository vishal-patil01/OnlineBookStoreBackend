package com.enigma.bookstore.service.implementation;

import com.enigma.bookstore.enums.FilterAttributes;
import com.enigma.bookstore.model.Book;
import com.enigma.bookstore.repository.IBookRepository;
import com.enigma.bookstore.service.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class BookService implements IBookService {


    @Autowired
    IBookRepository bookRepository;

    @Override
    public Page<Book> fetchBooks(String searchText, int pageNo, FilterAttributes filterAttributes) {
        return null;
    }
}
