package com.enigma.bookstore.controller;

import com.enigma.bookstore.dto.BookDTO;
import com.enigma.bookstore.dto.CartDTO;
import com.enigma.bookstore.dto.Response;
import com.enigma.bookstore.exception.BookException;
import com.enigma.bookstore.exception.CartException;
import com.enigma.bookstore.exception.CartItemsException;
import com.enigma.bookstore.model.Book;
import com.enigma.bookstore.model.Cart;
import com.enigma.bookstore.model.CartItems;
import com.enigma.bookstore.service.ICartService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(CartController.class)
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ICartService cartService;

    Gson gson = new Gson();
    Cart cart = new Cart();
    BookDTO bookDTO;

    @Test
    void givenCartData_WhenAllValidationAreTrue_ShouldReturnBookAddedToCartMessage() throws Exception {
        CartDTO cartDTO = new CartDTO(1, 2);
        bookDTO = new BookDTO("3436456546654", "Wings Of Fire", "A. P. J. Abdul Kalam", 400.0, 2, "Story Of Abdul Kalam", "/temp/pic01", 2014);
        Book book = new Book(bookDTO);
        CartItems cartBook = new CartItems(cartDTO, book, cart);
        String jsonData = gson.toJson(cartBook);
        String message = "Book Added To Cart successful";
        when(cartService.addToCart(any(), any())).thenReturn(message);
        MvcResult mvcResult = this.mockMvc.perform(post("/bookstore/cart/").contentType(MediaType.APPLICATION_JSON)
                .content(jsonData)).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        Response responseDto = gson.fromJson(response, Response.class);
        String responseMessage = responseDto.message;
        Assert.assertEquals(message, responseMessage);
    }

    @Test
    void givenCartData_WhenBookAlreadyAddedToCart_ShouldThrowCartException() throws Exception {
        CartDTO cartDTO = new CartDTO(1, 2);
        bookDTO = new BookDTO("3436456546654", "Wings Of Fire", "A. P. J. Abdul Kalam", 400.0, 2, "Story Of Abdul Kalam", "/temp/pic01", 2014);
        Book book = new Book(bookDTO);
        CartItems cartBook = new CartItems(cartDTO, book, cart);
        String jsonData = gson.toJson(cartBook);
        String message = "Book Already Added In Cart";
        when(cartService.addToCart(any(), any())).thenThrow(new CartItemsException("Book Already Added In Cart"));
        MvcResult mvcResult = this.mockMvc.perform(post("/bookstore/cart/").contentType(MediaType.APPLICATION_JSON)
                .content(jsonData)).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        Response responseDto = gson.fromJson(response, Response.class);
        String responseMessage = responseDto.message;
        Assert.assertEquals(message, responseMessage);
    }
    @Test
    void givenCartData_WhenBookNotFound_ShouldThrowBookException() throws Exception {
        CartDTO cartDTO = new CartDTO(1, 2);
        bookDTO = new BookDTO("3436456546654", "Wings Of Fire", "A. P. J. Abdul Kalam", 400.0, 2, "Story Of Abdul Kalam", "/temp/pic01", 2014);
        Book book = new Book(bookDTO);
        CartItems cartBook = new CartItems(cartDTO, book, cart);
        String jsonData = gson.toJson(cartBook);
        String message = "Book Not Found";
        when(cartService.addToCart(any(), any())).thenThrow(new BookException("Book Not Found"));
        MvcResult mvcResult = this.mockMvc.perform(post("/bookstore/cart/").contentType(MediaType.APPLICATION_JSON)
                .content(jsonData)).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        Response responseDto = gson.fromJson(response, Response.class);
        String responseMessage = responseDto.message;
        Assert.assertEquals(message, responseMessage);
    }
}

