package com.enigma.bookstore.controller;

import com.enigma.bookstore.Controller.AdminController;
import com.enigma.bookstore.Service.Implementation.AdminService;
import com.enigma.bookstore.dao.BookDAO;
import com.enigma.bookstore.model.BookDetails;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;
    BookDAO bookDAO;
    Gson gson = new Gson();

    @Test
    void givenBookDetails_WhenAdded_ShouldReturnsBooks() throws Exception {
        bookDAO = new BookDAO("Wings Of Fire", "A. P. J. Abdul Kalam", 400.0, 2, "Story Of Abdul Kalam", "/temp/pic01", 2014);
        BookDetails bookDetails = new BookDetails(bookDAO);
        String jsonDAO = gson.toJson(bookDetails);
        when(adminService.addBook(any())).thenReturn(bookDetails);

        this.mockMvc.perform(post("/bookstore/admin/addbook").content(jsonDAO)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonDAO));
    }

    @Test
    void givenRequest_WhenGetResponse_ItShouldReturnStatusOk() throws Exception {
        bookDAO = new BookDAO("Wings Of Fire", "A. P. J. Abdul Kalam", 400.0, 2, "Story Of Abdul Kalam", "/temp/pic01", 2014);
        String jsonDto = gson.toJson(bookDAO);
        BookDetails bookDetails = new BookDetails(bookDAO);
        when(adminService.addBook(any())).thenReturn(bookDetails);
        this.mockMvc.perform(post("/bookstore/admin/addbook").content(jsonDto)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    void givenWrongUrl_WhenGetResponse_ShouldReturn404Error() throws Exception {
        bookDAO = new BookDAO("Wings Of Fire", "A. P. J. Abdul Kalam", 400.0, 2, "Story Of Abdul Kalam", "/temp/pic01", 2014);
        String jsonDto = gson.toJson(bookDAO);
        this.mockMvc.perform(post("/addmybook").content(jsonDto)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    void givenGetRequestInsteadOfPost_ShouldReturn405Error() throws Exception {
        bookDAO = new BookDAO("Wings Of Fire", "A. P. J. Abdul Kalam", 400.0, 2, "Story Of Abdul Kalam", "/temp/pic01", 2014);
        String jsonDto = gson.toJson(bookDAO);
        this.mockMvc.perform(get("/bookstore/admin/addbook").content(jsonDto)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isMethodNotAllowed());
    }

    @Test
    void givenAnotherContentType_WhenGetResponse_ShouldReturn415error() throws Exception {
        bookDAO = new BookDAO("Wings Of Fire", "A. P. J. Abdul Kalam", 400.0, 2, "Story Of Abdul Kalam", "/temp/pic01", 2014);
        String jsonDto = gson.toJson(bookDAO);
        this.mockMvc.perform(post("/bookstore/admin/addbook").content(jsonDto)
                .contentType(MediaType.APPLICATION_ATOM_XML_VALUE)).andExpect(status().isUnsupportedMediaType());

    }

    @Test
    void givenRequestWithoutConvertItToJson_WhenGetResponse_ItsStatusShouldReturnBad() throws Exception {
        bookDAO = new BookDAO("Wings Of Fire", "A. P. J. Abdul Kalam", 400.0, 2, "Story Of Abdul Kalam", "/temp/pic01", 2014);
        MvcResult mvcResult = this.mockMvc.perform(post("/bookstore/admin/addbook").content(String.valueOf(bookDAO))
                .contentType(MediaType.APPLICATION_JSON)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        Assert.assertEquals(400, status);
    }
}
