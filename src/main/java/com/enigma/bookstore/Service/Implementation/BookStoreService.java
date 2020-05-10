package com.enigma.bookstore.service.implementation;

import com.enigma.bookstore.dto.BookDTO;
import com.enigma.bookstore.dto.Response;
import com.enigma.bookstore.exception.BookStoreException;
import com.enigma.bookstore.model.Book;
import com.enigma.bookstore.repository.IBookStoreRepository;
import com.enigma.bookstore.service.IBookStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookStoreService implements IBookStoreService {

    @Autowired
    IBookStoreRepository iBookStoreRepository;

    @Override
    public Response getAllBooks(int pageStart, int pageEnd) {
        Pageable pageRequest = PageRequest.of(pageStart, pageEnd);
        Page<Book> bookList = iBookStoreRepository.findAll(pageRequest);
        if (bookList.hasContent()) {
            List<BookDTO> allBookList = bookList.stream()
                    .map(BookDTO::new)
                    .collect(Collectors.toList());
            return new Response(allBookList, 200);
        }
        throw new BookStoreException("There Are No Books Available");
    }

    @Override
    public int getTotalBookCount() {
        return iBookStoreRepository.findAll().size();
    }
}
