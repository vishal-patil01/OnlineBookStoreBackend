package com.enigma.bookstore.model;

import com.enigma.bookstore.dto.BookDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "book")
public class Book implements Serializable {

    @NotNull
    @Column(unique = true, nullable = false)
    @Length(min = 10, max = 13, message = "Invalid ISBN Number")
    private String isbnNumber;

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "native"
    )
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
    private Integer id;

    @NotNull
    @Length(min = 2, max = 50, message = "Invalid Book Name")
    private String bookName;

    @NotNull
    @Pattern(regexp = "^[A-za-z][A-Za-z .]{3,}$", message = "Invalid Author Name.")
    private String authorName;

    @NotNull
    private double bookPrice;

    @NotNull
    private int noOfCopies;

    @Length(min = 10, max = 1000, message = "Minimum Details Should Be More Than 10 Characters.")
    private String bookDetail;
    private String bookImageSrc;

    @NotNull
    @Range(min = 1500, max = 2020, message = "Invalid Publishing Year.")
    private int publishingYear;

    public Book(BookDTO bookDTO) {
        this.isbnNumber = bookDTO.isbnNumber;
        this.bookName = bookDTO.bookName;
        this.authorName = bookDTO.authorName;
        this.bookPrice = bookDTO.bookPrice;
        this.noOfCopies = bookDTO.noOfCopies;
        this.bookDetail = bookDTO.bookDetail;
        this.bookImageSrc = bookDTO.bookImageSrc;
        this.publishingYear = bookDTO.publishingYear;
    }

    public void updateBook(BookDTO bookDTO) {
        this.isbnNumber = bookDTO.isbnNumber;
        this.bookName = bookDTO.bookName;
        this.authorName = bookDTO.authorName;
        this.bookPrice = bookDTO.bookPrice;
        this.noOfCopies = bookDTO.noOfCopies;
        this.bookDetail = bookDTO.bookDetail;
        this.bookImageSrc = bookDTO.bookImageSrc;
        this.publishingYear = bookDTO.publishingYear;
    }
}