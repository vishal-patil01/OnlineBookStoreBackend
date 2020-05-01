package com.enigma.bookstore.model;

import com.enigma.bookstore.dao.BookDAO;

import javax.persistence.*;

@Entity

@Table(name = "bookdetails")
public class BookDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer id;

    public String bookName;
    public String authorName;
    public double bookPrice;
    public double noOfCopies;
    public String bookDetail;
    public String bookImageSrc;
    public int publishingYear;

    public BookDetails(BookDAO bookDAO) {
        this.bookName = bookDAO.bookName;
        this.authorName = bookDAO.authorName;
        this.bookPrice = bookDAO.bookPrice;
        this.noOfCopies = bookDAO.noOfCopies;
        this.bookDetail = bookDAO.bookDetail;
        this.bookImageSrc = bookDAO.bookImageSrc;
        this.publishingYear = bookDAO.publishingYear;
    }
}
