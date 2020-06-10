package com.enigma.bookstore.dto;

import com.enigma.bookstore.model.Book;
import com.enigma.bookstore.model.CartItems;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmailTemplateDTO {
    public String email;
    public String subject;
    public String message;
    public List<Book> bookList;
}
