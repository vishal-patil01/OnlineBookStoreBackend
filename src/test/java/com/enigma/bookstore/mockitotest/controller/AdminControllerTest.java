package com.enigma.bookstore.mockitotest.controller;

import com.enigma.bookstore.controller.AdminController;
import com.enigma.bookstore.dto.BookDTO;
import com.enigma.bookstore.dto.Response;
import com.enigma.bookstore.service.implementation.AdminService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;
    BookDTO bookDTO;
    Gson gson = new Gson();

    @Test
    void givenRequest_WhenGetResponse_ItShouldReturnStatusOk() throws Exception {
        bookDTO = new BookDTO("136655645456L", "Wings Of Fire", "Abdul Kalam", 400, 2, "Story Of Abdul Kalam", "/temp/pic01", 2014);
        String jsonDto = gson.toJson(bookDTO);
        Response response = new Response(200, "ADDED");
        when(adminService.addBook(any())).thenReturn(response);
        this.mockMvc.perform(post("/bookstore/admin/book").content(jsonDto)
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
}
