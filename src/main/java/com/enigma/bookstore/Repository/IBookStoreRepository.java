package com.enigma.bookstore.Repository;

import com.enigma.bookstore.model.BookDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBookStoreRepository extends JpaRepository<BookDetails, Integer> {

}
