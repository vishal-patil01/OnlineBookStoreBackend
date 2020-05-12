package com.enigma.bookstore.dto;

import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
public class BookDTO {

    @NotNull
    @Column(unique = true, nullable = false)
    @Length(min = 10, max = 13, message = "Invalid ISBN Number")
    public String isbnNumber;

    @NotNull
    @Length(min = 2, max = 50, message = "Invalid Book Name")
    public String bookName;

    @NotNull
    @Pattern(regexp = "^[A-za-z][A-Za-z .]{3,}$", message = "Invalid Author Name.")
    public String authorName;

    @NotNull
    public double bookPrice;

    @NotNull
    public int noOfCopies;

    @Length(min = 10, max = 1000, message = "Minimum Details Should Be More Than 10 Characters.")
    public String bookDetail;

    public String bookImageSrc;

    @NotNull
    @Range(min = 1500, max = 2020, message = "Invalid Publishing Year.")
    public int publishingYear;
}