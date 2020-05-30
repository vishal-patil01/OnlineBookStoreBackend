package com.enigma.bookstore.service.implementation;

import com.enigma.bookstore.exception.CartException;
import com.enigma.bookstore.exception.UserException;
import com.enigma.bookstore.model.*;
import com.enigma.bookstore.repository.*;
import com.enigma.bookstore.service.IOrderService;
import com.enigma.bookstore.util.EmailTemplateGenerator;
import com.enigma.bookstore.util.IJWTToken;
import com.enigma.bookstore.util.IMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService implements IOrderService {
    @Autowired
    IJWTToken jwtToken;
    @Autowired
    IMailService mailService;
    @Autowired
    IOrderRepository orderRepository;
    @Autowired
    ICartItemsRepository cartItemsRepository;
    @Autowired
    IBookRepository bookRepository;
    @Autowired
    IUserRepository userRepository;
    @Autowired
    ICustomerRepository customerRepository;
    @Autowired
    ICartRepository cartRepository;
    @Autowired
    EmailTemplateGenerator orderEmailTemplate;

    @Autowired
    IOrderProductsRepository orderProductsRepository;

    String subject = "Your Order Placed Successfully";

    @Override
    public Integer placeOrder(Double totalPrice, String token) {
        Cart cart = checkUserAndCartIsExists(token);
        User user = userRepository.findById(jwtToken.verifyToken(token)).orElseThrow(() -> new UserException("User Not Found"));
        Customer customerDetails = customerRepository.findByUserOrderByCustomerIdDesc(cart.getUser()).get(0);
        Orders orders = new Orders(cart.getUser(), totalPrice, customerDetails, generateOrderId());
        Orders savedOrder = orderRepository.save(orders);
        List<CartItems> cartItemsList = cartItemsRepository.findAllByCart_CardId(cart.getCardId());
        cartItemsList.forEach(cartBook -> bookRepository
                .updateBookQuantity(cartBook.getBook().getId(), cartBook.getQuantity()));
        cartItemsList.forEach(cartBook -> {
            OrderProducts orderProducts = new OrderProducts(cartBook.getBook(), orders, cartBook.getQuantity());
            orderProductsRepository.save(orderProducts);
        });
        String customerAddress = customerDetails.customerAddress + " " + customerDetails.customerLandmark + " " + customerDetails.customerTown + ", " + customerDetails.customerPinCode;
        String message = orderEmailTemplate.getHeader(user.getFullName())
                + orderEmailTemplate.getOrderPlacedTemplate(cartItemsList, totalPrice, getFormattedDate(new Timestamp(System.currentTimeMillis()).toString()), customerAddress, savedOrder.getOrderId())
                + orderEmailTemplate.getFooter();
        cartItemsRepository.deleteCartItems(cart.getCardId());
        mailService.sendEmail(user.getEmail(), subject, message, cartItemsList);
        return savedOrder.getOrderId();
    }

    @Override
    public List<Orders> fetchOrders(String token) {
        return null;
    }

    private String getFormattedDate(String timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss.SSS");
        String orderDate = "";
        try {
            Date date = sdf.parse(timeStamp);
            sdf.applyPattern("dd MMMM yyyy");
            orderDate = sdf.format(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return orderDate;
    }

    private Cart checkUserAndCartIsExists(String token) {
        int userId = jwtToken.verifyToken(token);
        userRepository.findById(userId)
                .orElseThrow(() -> new UserException("User Not Exist"));
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartException("Cart Not Found"));
    }

    private Integer generateOrderId() {
        boolean isUnique = false;
        int orderId = 0;
        while (!isUnique) {
            orderId = (int) Math.floor(100000 + Math.random() * 999999);
            Optional<Orders> orders = orderRepository.findByOrderId(orderId);
            if (!orders.isPresent())
                isUnique = true;
        }
        return orderId;
    }
}
