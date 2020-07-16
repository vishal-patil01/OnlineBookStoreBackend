package com.enigma.bookstore.controller;

import com.enigma.bookstore.dto.BookDTO;
import com.enigma.bookstore.dto.Response;
import com.enigma.bookstore.exception.WishListItemsException;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

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
    void givenWishListData_WhenAllValidationAreTrue_ShouldReturnBookAddedToWishListMessage() throws Exception {
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

    @Test
    void givenRequest_WhenWishListIsNotEmpty_ShouldReturnWishListRecords() throws Exception {
        bookDTO = new BookDTO("3436456546654", "Wings Of Fire", "A. P. J. Abdul Kalam", 400.0, 2, "Story Of Abdul Kalam", "/temp/pic01", 2014);
        Book book = new Book(bookDTO);
        WishListItems wishListItems = new WishListItems(book, new WishList());
        String message = "Wings Of Fire";
        wishList.add(wishListItems);
        when(wishListService.fetchWishList(any())).thenReturn(wishList);
        MvcResult mvcResult = this.mockMvc.perform(get("/bookstore/wishlist")).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        Assert.assertTrue(response.contains(message));
    }

    @Test
    void givenRequest_WhenWishListIsEmpty_ShouldReturnWishListRecords() throws Exception {
        bookDTO = new BookDTO("3436456546654", "Wings Of Fire", "A. P. J. Abdul Kalam", 400.0, 2, "Story Of Abdul Kalam", "/temp/pic01", 2014);
        Book book = new Book(bookDTO);
        WishListItems wishListItems = new WishListItems(book, new WishList());
        wishList.add(wishListItems);
        when(wishListService.fetchWishList(any())).thenThrow(new WishListItemsException("No Such Book In Wish List"));
        MvcResult mvcResult = this.mockMvc.perform(get("/bookstore/wishlist")).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        Response responseDto = gson.fromJson(response, Response.class);
        Assert.assertEquals("No Such Book In Wish List", responseDto.message);
    }

    @Test
    void givenBookId_WhenBookIsPresent_ShouldReturnBookRemovedFromWishListSuccessfullyMessage() throws Exception {
        String message = "Book Removed From WishList successfully";
        when(wishListService.deleteBookFromWishList(anyInt(), any())).thenReturn(message);
        MvcResult mvcResult = this.mockMvc.perform(delete("/bookstore/wishlist/1")).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        Response responseDto = gson.fromJson(response, Response.class);
        String responseMessage = responseDto.message;
        Assert.assertEquals(message, responseMessage);
    }

}

