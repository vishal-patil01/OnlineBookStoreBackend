package com.enigma.bookstore.service.implementation;

import com.enigma.bookstore.dto.BookDTO;
import com.enigma.bookstore.dto.Response;
import com.enigma.bookstore.exception.AdminException;
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

    @Autowired
    ModelMapper modelMapper;

    @Override
    public Response addBook(BookDTO bookDTO) {
        boolean byIsbnNumber = bookStoreRepository.findByIsbnNumber(bookDTO.getIsbnNumber()).isPresent();
        boolean byBookName = bookStoreRepository.findByBookNameAndAuthorName(bookDTO.getBookName(), bookDTO.getAuthorName()).isPresent();
        if (byIsbnNumber) {
            throw new AdminException("ISBN Number is already exists.");
        } else if (byBookName) {
            throw new AdminException("Book Name and Author Name is already exists.");
        }
        Book book = modelMapper.map(bookDTO, Book.class);
        bookStoreRepository.save(book);
        return new Response(200, "Book Added successfully.");
    }
}
