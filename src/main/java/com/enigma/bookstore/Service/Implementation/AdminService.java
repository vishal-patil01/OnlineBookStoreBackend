package com.enigma.bookstore.Service.Implementation;

import com.enigma.bookstore.dao.BookDAO;
import com.enigma.bookstore.model.BookDetails;
import com.enigma.bookstore.Repository.IBookStoreRepository;
import com.enigma.bookstore.Service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService implements IAdminService {

    @Autowired
    private IBookStoreRepository bookStoreRepository;

    @Override
    public BookDetails addBook(BookDAO bookDAO) {
        return null;
    }
}
