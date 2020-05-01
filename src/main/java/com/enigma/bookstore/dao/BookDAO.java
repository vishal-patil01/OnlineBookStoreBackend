package com.enigma.bookstore.dao;

public class BookDAO {

    public String bookName;
    public String authorName;
    public double bookPrice;
    public double noOfCopies;
    public String bookDetail;
    public String bookImageSrc;

    public int publishingYear;

    public BookDAO(String bookName, String authorName, double bookPrice, double noOfCopies, String bookDetail, String bookImageSrc, int publishingYear) {
        this.bookName = bookName;
        this.authorName = authorName;
        this.bookPrice = bookPrice;
        this.noOfCopies = noOfCopies;
        this.bookDetail = bookDetail;
        this.bookImageSrc = bookImageSrc;
        this.publishingYear = publishingYear;
    }
}
