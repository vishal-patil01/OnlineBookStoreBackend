package com.enigma.bookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class BookDTO {

    @NotNull
    @Column(unique = true, nullable = false)
    @Length(min = 10, max = 13, message = "Invalid ISBN Number")
    private String isbnNumber;

    @NotNull
    @Length(min = 2, max = 50, message = "Invalid Book Name 2 to 50 Character Required.")
    private String bookName;

    @NotNull
    @Length(min = 3, message = "Invalid Author Name Minimum 3 Character Required.")
    private String authorName;

    @NotNull
    private double bookPrice;

    @NotNull
    private int noOfCopies;

    @Length(min = 10, max = 1000, message = "Minimum Details Should Be More Than 10 Characters.")
    private String bookDetail;

    private String bookImageSrc;

    @NotNull
    @Range(min = 1000, max = 2021, message = "Invalid Publishing Year.")
    private int publishingYear;
}