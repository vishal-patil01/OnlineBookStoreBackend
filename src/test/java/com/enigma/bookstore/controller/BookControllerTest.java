package com.enigma.bookstore.controller;

import com.enigma.bookstore.dto.BookDTO;
import com.enigma.bookstore.dto.Response;
import com.enigma.bookstore.enums.FilterAttributes;
import com.enigma.bookstore.exception.BookException;
import com.enigma.bookstore.model.Book;
import com.enigma.bookstore.service.implementation.BookService;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(BookController.class)
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    List<Book> bookList = new ArrayList<>();
    BookDTO bookDTO;
    Gson gson = new Gson();


    @Test
    void givenRequest_WhenGetResponse_ItShouldReturnBooks() throws Exception {
        bookDTO = new BookDTO("136655645456L", "Wings Of Fire", "A. P. J. Abdul Kalam", 400.0, 2, "Story Of Abdul Kalam", "/temp/pic01", 2014);
        Book book = new Book(bookDTO);
        bookList.add(book);
        Page<Book> bookPage = new PageImpl(bookList);
        String bookListInJsonForm = gson.toJson(bookList);
        when(bookService.fetchBooks(any(),anyInt(),any())).thenReturn(bookPage);
        MvcResult mvcResult = this.mockMvc.perform(get("/bookstore/books/?pageno=1").content(bookListInJsonForm)
                .contentType(MediaType.APPLICATION_JSON)).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        Response responseDto = gson.fromJson(response, Response.class);
        String responseMessage = gson.toJson(responseDto.data);
        Assert.assertEquals(bookListInJsonForm.contains("Wings Of Fire"), responseMessage.contains("Wings Of Fire"));
    }

    @Test
    void givenRequest_WhenNoRecordFound_ShouldThrowException() {
        try {
            when(bookService.fetchBooks(anyString(), anyInt(), any())).thenThrow(new BookException("There Are No Books Available"));
            bookService.fetchBooks("zzz", 1, FilterAttributes.DEFAULT);
        } catch (BookException e) {
            Assert.assertSame("There Are No Books Available", e.getMessage());
        }
    }
    @Test
    void givenFileName_WhenFound_ShouldReturnFile() throws Exception {
        String fileName="AnneFrank.jpg";
        String imagePath="/src/main/resources/Images/";
        String fileBasePath = System.getProperty("user.dir")+imagePath;
        Path path = Paths.get(fileBasePath + fileName);
        Resource resource = new UrlResource(path.toUri());
        when(bookService.loadImages(any())).thenReturn(resource);
        MvcResult result = this.mockMvc.perform(get("/bookstore/image/AnneFrank.jpg"))
                .andReturn();
        Assert.assertEquals(200,result.getResponse().getStatus());
    }
}
