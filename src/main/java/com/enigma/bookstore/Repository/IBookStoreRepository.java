package com.enigma.bookstore.repository;

import com.enigma.bookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IBookStoreRepository extends JpaRepository<Book, Integer> {
    Optional<Book> findByIsbnNumber(String isbnNumber);

    Optional<Book> findByBookNameAndAuthorName(String bookName, String authorName);
}
