package com.enigma.bookstore.service.implementation;

import com.enigma.bookstore.enums.FilterAttributes;
import com.enigma.bookstore.exception.BookException;
import com.enigma.bookstore.model.Book;
import com.enigma.bookstore.properties.ApplicationProperties;
import com.enigma.bookstore.repository.IBookRepository;
import com.enigma.bookstore.service.IBookService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class BookService implements IBookService {

    @Autowired
    IBookRepository bookRepository;

    @Autowired
    private ApplicationProperties applicationProperties;


    @Override
    @Cacheable(cacheNames = "books")
    public Page<Book> fetchBooks(String searchText, int pageNo, FilterAttributes filterAttributes) {
        if (pageNo <= 0)
            throw new BookException("Invalid Page Number");
        System.out.println("fetched from db");
        PageRequest pageRequest = PageRequest.of(pageNo - 1, 12, filterAttributes.sort);
        Page<Book> books = bookRepository.fetchBooks(pageRequest, searchText);
        if (books.hasContent())
            return books;
        throw new BookException("There Are No Books Available");
    }

    @SneakyThrows
    @Override
    public Resource loadImages(String fileName) {
        String fileBasePath = System.getProperty("user.dir") + applicationProperties.getUploadDir();
        Path path = Paths.get(fileBasePath + fileName);
        Resource resource = new UrlResource(path.toUri());
        if (resource.exists())
            return resource;
        throw new BookException("Image not found " + fileName);
    }
}
