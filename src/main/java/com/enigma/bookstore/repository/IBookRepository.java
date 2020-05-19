package com.enigma.bookstore.repository;

import com.enigma.bookstore.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface IBookRepository extends JpaRepository<Book, Integer> {
    Optional<Book> findByIsbnNumber(String isbnNumber);

    Optional<Book> findByBookNameAndAuthorName(String bookName, String authorName);
    
    @Query(value = "select * from book where author_name LIKE %:toSearch% OR book_name LIKE %:toSearch%", nativeQuery = true)
    Page<Book> fetchBooks(Pageable pageable, @Param("toSearch") String toSearch);

    @Transactional
    @Modifying
    @Query(value = "update book set no_of_copies = no_of_copies - :quantity where id = :bid ", nativeQuery = true)
    int updateBookQuantity(@Param("bid") Integer cid,@Param("quantity") Integer quantity);

}
