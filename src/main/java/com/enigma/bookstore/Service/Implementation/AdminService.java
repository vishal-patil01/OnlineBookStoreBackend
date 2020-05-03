package com.enigma.bookstore.service.implementation;

import com.enigma.bookstore.dto.BookDTO;
import com.enigma.bookstore.dto.Response;
import com.enigma.bookstore.exception.AdminException;
import com.enigma.bookstore.model.Book;
import com.enigma.bookstore.repository.IBookStoreRepository;
import com.enigma.bookstore.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService implements IAdminService {

    @Autowired
    private IBookStoreRepository bookStoreRepository;


    @Override
    public Response addBook(BookDTO bookDTO) {
        Book book = new Book(bookDTO);
        boolean byIsbnNumber = bookStoreRepository.findByIsbnNumber(bookDTO.getIsbnNumber()).isPresent();
        if (byIsbnNumber)
            throw new AdminException("ISBN Number is already exists.", AdminException.ExceptionType.ISBN_NUMBER_ALREADY_EXISTS);
        bookStoreRepository.save(book);
        return new Response(200, "Book Added successfully.");
    }
}
