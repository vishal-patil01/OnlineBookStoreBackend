package com.enigma.bookstore.service;

import com.enigma.bookstore.configuration.ConfigureRabbitMq;
import com.enigma.bookstore.dto.BookDTO;
import com.enigma.bookstore.dto.CartDTO;
import com.enigma.bookstore.dto.CustomerDTO;
import com.enigma.bookstore.dto.UserRegistrationDTO;
import com.enigma.bookstore.enums.AddressType;
import com.enigma.bookstore.enums.UserRole;
import com.enigma.bookstore.exception.OrderException;
import com.enigma.bookstore.model.*;
import com.enigma.bookstore.properties.ApplicationProperties;
import com.enigma.bookstore.repository.*;
import com.enigma.bookstore.util.EmailTemplateGenerator;
import com.enigma.bookstore.util.IMailService;
import com.enigma.bookstore.util.implementation.JWTToken;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OrderServiceTest {


    @Autowired
    IOrderService orderBookService;

    @MockBean
    ConfigureRabbitMq configureRabbitMq;

    @MockBean
    ApplicationProperties applicationProperties;

    @MockBean
    IBookRepository onlineBookStoreRepository;

    @MockBean
    IUserRepository userRepository;

    @MockBean
    ICustomerRepository customerDetailsRepository;

    @MockBean
    EmailTemplateGenerator emailTemplateGenerator;

    @MockBean
    IMailService mailService;

    @MockBean
    IOrderProductsRepository orderProductRepository;

    @MockBean
    IOrderRepository orderRepository;

    @MockBean
    ICartItemsRepository bookCartRepository;

    @MockBean
    ICartRepository cartRepository;

    @MockBean
    JWTToken tokenGenerator;

    BookDTO bookDTO;
    Book bookDetails;
    CustomerDTO customerDTO;
    Customer customerDetails;
    UserRegistrationDTO userRegistrationDTO;
    User userDetails;
    Cart cart;
    CartDTO cartDTO;
    List<Customer> customerDetailsList;
    CartItems bookCart;
    List<CartItems> bookCartList;
    Orders orders;

    public OrderServiceTest() {
        bookDTO = new BookDTO("1234567890", "YOU WERE MY CRUSH: Till You Said You Love Me!", "Durjoy Datta", 400.0, 10, "Devotional", "book image", 2002);
        bookDetails = new Book(bookDTO);
        bookDetails.setId(1);
        bookCart = new CartItems();
        cartDTO = new CartDTO(1, 2);
        cart = new Cart();
        bookCart = new CartItems(cartDTO, bookDetails, cart);
        bookCart.setBook(bookDetails);
        bookCart.setCartItemsId(1);
        bookCart.setQuantity(2);
        bookCartList = new ArrayList<>();
        bookCartList.add(bookCart);
        cart.setCartItems(bookCartList);
        userRegistrationDTO = new UserRegistrationDTO("Sam", "sam@gmail.com", "Sam@12345", "8855885588", false, UserRole.USER);
        userDetails = new User(userRegistrationDTO);
        cart.setUser(userDetails);
        customerDTO = new CustomerDTO(425001, "Mumbai", "Shiv Colony", "Mumbai", "Thane", AddressType.HOME);
        customerDetails = new Customer(customerDTO, userDetails);
        customerDetailsList = new ArrayList<>();
        customerDetailsList.add(customerDetails);
        orders = new Orders(userDetails, 1400.0, customerDetails, 123456);

    }

    @Test
    void givenCustomerOrderDetailsToAddInDatabase_WhenAdded_ShouldReturnOrderId() {
        when(tokenGenerator.verifyToken(any())).thenReturn(1);
        when(userRepository.findById(any())).thenReturn(java.util.Optional.of(userDetails));
        when(cartRepository.findByUserId(any())).thenReturn(java.util.Optional.of(new Cart()));
        when(customerDetailsRepository.findByUserIdAndAndCustomerAddressType(anyInt(), any())).thenReturn(customerDetailsList);
        when(customerDetailsRepository.findByUserOrderByCustomerIdDesc(any())).thenReturn(customerDetailsList);
        when(bookCartRepository.findByBookIdAndCart_CardId(anyInt(), anyInt())).thenReturn(bookCartList);
        when(orderProductRepository.save(any())).thenReturn(new OrderProducts());
        when(orderRepository.save(any())).thenReturn(orders);
        when(bookCartRepository.deleteCartItems(anyInt())).thenReturn(1);
        when(emailTemplateGenerator.getHeader(any())).thenReturn("Header");
        when(emailTemplateGenerator.getOrderPlacedTemplate(any(), any(), any(), any(), any())).thenReturn("Header");
        when(emailTemplateGenerator.getFooter()).thenReturn("Footer");
        Integer message = orderBookService.placeOrder(1420.0, "authorization");
        Assert.assertEquals(123456, message, 0.0);
    }

    @Test
    void givenCustomerOrderDetailsToAddInDatabase_WhenAdded_ShouldReturnCorrectDetails1() {
        Orders orderBookDetails = new Orders(userDetails, 1000.0, customerDetails, 123456);
        List<Orders> orderBookDetailsList = new ArrayList<>();
        orderBookDetailsList.add(orderBookDetails);
        when(tokenGenerator.verifyToken(any())).thenReturn(1);
        when(cartRepository.findByUserId(any())).thenReturn(java.util.Optional.of(new Cart()));
        when(userRepository.findById(any())).thenReturn(java.util.Optional.of(userDetails));
        when(orderRepository.findOrdersByUser_IdOrderByOrderPlacedDateDesc(any())).thenReturn(orderBookDetailsList);
        List<Orders> orderBookDetailsList1 = orderBookService.fetchOrders("authorization");
        Assert.assertEquals(orderBookDetailsList, orderBookDetailsList1);
    }

    @Test
    void givenCustomerOrderDetailsToAddInDatabase_WhenAdded_ShouldReturnCorrectDetails2() {
        try {
            when(tokenGenerator.verifyToken(any())).thenReturn(1);
            when(cartRepository.findByUserId(any())).thenReturn(java.util.Optional.of(new Cart()));
            when(userRepository.findById(any())).thenReturn(java.util.Optional.of(userDetails));
            when(orderRepository.findOrdersByUser_IdOrderByOrderPlacedDateDesc(any())).thenThrow(new OrderException("There Is No Order Placed Yet"));
            orderBookService.fetchOrders("authorization");
        } catch (OrderException e) {
            Assert.assertEquals("There Is No Order Placed Yet", e.getMessage());
        }
    }
}
