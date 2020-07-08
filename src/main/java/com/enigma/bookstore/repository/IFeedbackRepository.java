package com.enigma.bookstore.repository;

import com.enigma.bookstore.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IFeedbackRepository extends JpaRepository<Feedback, Integer> {
    @Query(value = "select user_id from feedback where id=:feedbackId", nativeQuery = true)
    int getUserFeedbackId(@Param("feedbackId") int id);

    @Query(value = "select id from feedback where book_id=:bookId", nativeQuery = true)
    List<Integer> getFeedbackIds(@Param("bookId") int id);

    @Query(value = "select id from feedback where user_id=:userId and book_id=:bookId", nativeQuery = true)
    Integer getFeedbackId(@Param("userId") int userId, @Param("bookId") int bookId);
}
