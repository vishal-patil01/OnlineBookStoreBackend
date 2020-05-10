package com.enigma.bookstore.service.implementation;

import com.enigma.bookstore.dto.BookDTO;
import com.enigma.bookstore.dto.Response;
import com.enigma.bookstore.exception.BookStoreException;
import com.enigma.bookstore.model.Book;
import com.enigma.bookstore.repository.IBookStoreRepository;
import com.enigma.bookstore.service.IAdminService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService implements IAdminService {

    @Autowired
    private IBookStoreRepository bookStoreRepository;

    @Override
    public Response addBook(BookDTO bookDTO) {
        Book book = new Book(bookDTO);
        boolean byIsbnNumber = bookStoreRepository.findByIsbnNumber(bookDTO.isbnNumber).isPresent();
        boolean byBookName = bookStoreRepository.findByBookNameAndAuthorName(bookDTO.bookName, bookDTO.authorName).isPresent();
        if (byIsbnNumber) {
            throw new BookStoreException("ISBN Number is already exists.");
        } else if (byBookName) {
            throw new BookStoreException("Book Name and Author Name is already exists.");
        }
        bookStoreRepository.save(book);
        return new Response("Book Added successfully.", 200);
    }
}
