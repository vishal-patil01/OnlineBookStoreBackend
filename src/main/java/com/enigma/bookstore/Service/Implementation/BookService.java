package com.enigma.bookstore.service.implementation;

import com.enigma.bookstore.enums.FilterAttributes;
import com.enigma.bookstore.exception.BookException;
import com.enigma.bookstore.model.Book;
import com.enigma.bookstore.repository.IBookRepository;
import com.enigma.bookstore.service.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class BookService implements IBookService {


    @Autowired
    IBookRepository bookRepository;

    @Override
    public Page<Book> fetchBooks(String searchText, int pageNo, FilterAttributes filterAttributes) {
        PageRequest pageRequest = PageRequest.of(pageNo-1, 12, filterAttributes.sort);
        Page<Book> books = bookRepository.fetchBooks(pageRequest, searchText);
        if (books.hasContent())
            return books;
        throw new BookException("There Are No Books Available");
    }
}
