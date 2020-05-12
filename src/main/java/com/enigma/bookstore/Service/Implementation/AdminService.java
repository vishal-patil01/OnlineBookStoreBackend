package com.enigma.bookstore.service.implementation;

import com.enigma.bookstore.dto.BookDTO;
import com.enigma.bookstore.exception.BookException;
import com.enigma.bookstore.model.Book;
import com.enigma.bookstore.repository.IBookRepository;
import com.enigma.bookstore.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService implements IAdminService {

    @Autowired
    private IBookRepository bookRepository;

    @Override
    public String addBook(BookDTO bookDTO) {
        Book book = new Book(bookDTO);
        boolean isIsbnNumberPresent = bookRepository.findByIsbnNumber(bookDTO.isbnNumber).isPresent();
        boolean isBookNamePresent = bookRepository.findByBookNameAndAuthorName(bookDTO.bookName, bookDTO.authorName).isPresent();
        if (isIsbnNumberPresent) {
            throw new BookException("ISBN Number is already exists.");
        } else if (isBookNamePresent) {
            throw new BookException("Book Name and Author Name is already exists.");
        }
        bookRepository.save(book);
        return "Book Added successfully.";
    }
}
