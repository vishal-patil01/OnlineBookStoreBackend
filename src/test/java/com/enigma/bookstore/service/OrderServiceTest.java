package com.enigma.bookstore.service;

import com.enigma.bookstore.configuration.ConfigureRabbitMq;
import com.enigma.bookstore.dto.*;
import com.enigma.bookstore.enums.AddressType;
import com.enigma.bookstore.enums.UserRole;
import com.enigma.bookstore.exception.OrderException;
import com.enigma.bookstore.model.*;
import com.enigma.bookstore.properties.ApplicationProperties;
import com.enigma.bookstore.repository.*;
import com.enigma.bookstore.util.EmailTemplateGenerator;
import com.enigma.bookstore.util.IMailService;
import com.enigma.bookstore.util.ITokenGenerator;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;

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
    IMailService mailService;

    @MockBean
    IOrderProductsRepository orderProductRepository;

    @MockBean
    IOrderRepository orderRepository;

    @MockBean
    ICartRepository cartRepository;

    @MockBean
    ITokenGenerator tokenGenerator;

    @MockBean
    ICartItemsRepository cartItemsRepository;

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
    List<Orders> ordersList = new ArrayList<>();
    Orders orders;
    Page<Orders> page;
    CartItems cartItems;
    List<CartItems> cartItemsList;
    Book book;

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
        orders = new Orders(userDetails, 1400.0, customerDetails, "00000001");
        ordersList.add(orders);
        Pageable pageRequest = PageRequest.of(0, 1, Sort.by("order_id"));
        page = new PageImpl<>(ordersList, pageRequest, 1);
        cartDTO = new CartDTO(1, 1);
        cart = new Cart();
        book = new Book(bookDTO);
        cartItems = new CartItems(cartDTO, book, cart);
        cartItemsList = new ArrayList<>();
        cartItemsList.add(cartItems);
        cart.setCardId(1);
        cart.setCartItems(cartItemsList);
    }

    @Test
    void givenCustomerOrderDetailsToAddInDatabase_WhenAdded_ShouldReturnOrderId() {
        when(tokenGenerator.verifyToken(any())).thenReturn(1);
        when(userRepository.findById(any())).thenReturn(java.util.Optional.of(userDetails));
        when(cartRepository.findByUserId(any())).thenReturn(java.util.Optional.of(new Cart()));
        when(customerDetailsRepository.findByUserIdAndAndCustomerAddressType(anyInt(), any())).thenReturn(customerDetailsList);
        when(customerDetailsRepository.findByUserIdAndAndCustomerAddressType(any(), any())).thenReturn(customerDetailsList);
        when(cartItemsRepository.findByBookIdAndCart_CardId(anyInt(), anyInt())).thenReturn(bookCartList);
        when(orderProductRepository.save(any())).thenReturn(new OrderProducts());
        when(orderRepository.fetchOrders(any())).thenReturn(page);
        when(orderRepository.save(any())).thenReturn(orders);
        when(cartItemsRepository.deleteCartItems(anyInt())).thenReturn(1);
        when(cartItemsRepository.findAllByCart_CardId(any())).thenReturn(cartItemsList);
        String message = orderBookService.placeOrder(new OrderDTO(AddressType.HOME, 124.0), "authorization");
        Assert.assertEquals("00000001", message);
    }

    @Test
    void givenCustomerOrderDetailsToAddInDatabase_WhenAdded_ShouldReturnCorrectDetails1() {
        Orders orderBookDetails = new Orders(userDetails, 1000.0, customerDetails, "00000001");
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
