package com.enigma.bookstore.service;

import com.enigma.bookstore.enums.FilterAttributes;
import com.enigma.bookstore.model.Book;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;

public interface IBookService {
    Page<Book> fetchBooks(String text, int pageNo, FilterAttributes filterAttributes);

    Resource loadImages(String fileName);
}
