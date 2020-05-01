package com.enigma.bookstore.Service;

import com.enigma.bookstore.dao.BookDAO;
import com.enigma.bookstore.model.BookDetails;

public interface IAdminService {
    BookDetails addBook(BookDAO bookDAO);
}
