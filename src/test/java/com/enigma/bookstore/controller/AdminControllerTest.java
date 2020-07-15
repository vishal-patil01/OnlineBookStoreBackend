package com.enigma.bookstore.controller;

import com.enigma.bookstore.dto.BookDTO;
import com.enigma.bookstore.dto.Response;
import com.enigma.bookstore.dto.UserLoginDTO;
import com.enigma.bookstore.exception.BookException;
import com.enigma.bookstore.model.Book;
import com.enigma.bookstore.service.implementation.AdminService;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    AdminController adminController;

    @MockBean
    private AdminService adminService;

    BookDTO bookDTO;
    Gson gson = new Gson();
    List<BookDTO> list = new ArrayList<>();

    @Test
    void givenRequest_WhenGetResponse_ItShouldReturnStatusOk() throws Exception {
        bookDTO = new BookDTO("3436456546654", "Wings Of Fire", "A. P. J. Abdul Kalam", 400.0, 2, "Story Of Abdul Kalam", "/temp/pic01", 2014);
        list.add(bookDTO);
        String toJson = gson.toJson(bookDTO);
        when(adminService.addBook(any(),any())).thenReturn("Added");
        this.mockMvc.perform(post("/bookstore/admin/book").content(toJson)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    void givenWrongUrl_WhenGetResponse_ShouldReturn404Error() throws Exception {
        bookDTO = new BookDTO("136655645456L", "Wings Of Fire", "Abdul Kalam", 400, 2, "Story Of Abdul Kalam", "/temp/pic01", 2014);
        String jsonDto = gson.toJson(bookDTO);
        this.mockMvc.perform(post("/book").content(jsonDto)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    void givenGetRequestInsteadOfPost_ShouldReturn405Error() throws Exception {
        bookDTO = new BookDTO("136655645456L", "Wings Of Fire", "A. P. J. Abdul Kalam", 400, 2, "Story Of Abdul Kalam", "/temp/pic01", 2014);
        String jsonDto = gson.toJson(bookDTO);
        this.mockMvc.perform(get("/bookstore/admin/book").content(jsonDto)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isMethodNotAllowed());
    }

    @Test
    void givenAnotherContentType_WhenGetResponse_ShouldReturn415error() throws Exception {
        bookDTO = new BookDTO("136655645456L", "Wings Of Fire", "A. P. J. Abdul Kalam", 400.0, 2, "Story Of Abdul Kalam", "/temp/pic01", 2014);
        String jsonDto = gson.toJson(bookDTO);
        this.mockMvc.perform(post("/bookstore/admin/book").content(jsonDto)
                .contentType(MediaType.APPLICATION_ATOM_XML_VALUE)).andExpect(status().isUnsupportedMediaType());

    }

    @Test
    void givenRequestWithoutJsonConversion_WhenGetResponse_ShouldReturnBadStatusCode() throws Exception {
        bookDTO = new BookDTO("136655645456L", "Wings Of Fire", "A. P. J. Abdul Kalam", 400.0, 2, "Story Of Abdul Kalam", "/temp/pic01", 2014);
        MvcResult mvcResult = this.mockMvc.perform(post("/bookstore/admin/book").content(String.valueOf(bookDTO))
                .contentType(MediaType.APPLICATION_JSON)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        Assert.assertEquals(400, status);
    }

    @Test
    void givenImageAsMultipart_shouldReturnImageViewURL() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile("file", "1.jpg",
                "image/jpg", "Some data".getBytes());
        MvcResult result = this.mockMvc.perform(multipart("/bookstore/admin/image")
                .file(imageFile))
                .andReturn();
        Assert.assertEquals(200, result.getResponse().getStatus());
    }

    // --
    @Test
    void givenBookDtoAndBookId_WhenGetResponse_ItShouldReturnBookUpdatedSuccessfully() throws Exception {
        bookDTO = new BookDTO("136655645456L", "Wings Of Fire", "A. P. J. Abdul Kalam", 400.0, 2, "Story Of Abdul Kalam", "/temp/pic01", 2014);
        Book book = new Book(bookDTO);
        String toJson = gson.toJson(book);
        when(adminService.updateBook(any(), any(),any())).thenReturn("Book Updated Successfully");
        MvcResult mvcResult = this.mockMvc.perform(post("/bookstore/admin/book/1").content(toJson)
                .contentType(MediaType.APPLICATION_JSON)).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        Response responseDto = gson.fromJson(response, Response.class);
        String responseMessage = responseDto.message;
        Assert.assertEquals("Book Updated Successfully", responseMessage);
    }

    @Test
    void givenUserLoginData_WhenAllValidationAreTrueAndEmailExists_ShouldReturnLoginSuccessfulMessage() throws Exception {
        UserLoginDTO loginDTO = new UserLoginDTO("sam@gmail.com", "Asdfg@123");
        String stringConvertDTO = gson.toJson(loginDTO);
        String message = "Login Successful";
        when(adminService.adminLogin(any())).thenReturn(message);
        MvcResult mvcResult = this.mockMvc.perform(post("/bookstore/admin/login").contentType(MediaType.APPLICATION_JSON)
                .content(stringConvertDTO)).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        Response responseDto = gson.fromJson(response, Response.class);
        String responseMessage = responseDto.message;
        Assert.assertEquals(message, responseMessage);
    }
}
