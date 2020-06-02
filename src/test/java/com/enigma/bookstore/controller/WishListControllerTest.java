package com.enigma.bookstore.controller;

import com.enigma.bookstore.dto.BookDTO;
import com.enigma.bookstore.dto.Response;
import com.enigma.bookstore.model.Book;
import com.enigma.bookstore.model.WishList;
import com.enigma.bookstore.model.WishListItems;
import com.enigma.bookstore.service.IWishListService;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(WishListController.class)
public class WishListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IWishListService wishListService;

    Gson gson = new Gson();
    Book book;
    List<WishListItems> wishList = new ArrayList<>();
    BookDTO bookDTO;

    @Test
    void givenCartData_WhenAllValidationAreTrue_ShouldReturnBookAddedToWishListMessage() throws Exception {
        WishListItems wishListItems = new WishListItems(book, new WishList());
        String jsonData = gson.toJson(wishListItems);
        String message = "Book Added To Wish List successful";
        when(wishListService.addToWishList(any(), any())).thenReturn(message);
        MvcResult mvcResult = this.mockMvc.perform(post("/bookstore/wishlist/1").contentType(MediaType.APPLICATION_JSON)
                .content(jsonData)).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        Response responseDto = gson.fromJson(response, Response.class);
        String responseMessage = responseDto.message;
        Assert.assertEquals(message, responseMessage);
    }
}

