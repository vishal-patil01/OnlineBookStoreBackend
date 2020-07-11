package com.enigma.bookstore.service.implementation;

import com.enigma.bookstore.exception.CartException;
import com.enigma.bookstore.exception.OrderException;
import com.enigma.bookstore.exception.UserException;
import com.enigma.bookstore.model.*;
import com.enigma.bookstore.repository.*;
import com.enigma.bookstore.service.IOrderService;
import com.enigma.bookstore.util.EmailTemplateGenerator;
import com.enigma.bookstore.util.IMailService;
import com.enigma.bookstore.util.ITokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService implements IOrderService {
    @Autowired
    ITokenGenerator jwtToken;
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
    public String placeOrder(Double totalPrice, String token) {
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
        List<Book> bookList = cartItemsList.stream().map(CartItems::getBook).collect(Collectors.toList());
        String customerAddress = customerDetails.customerAddress + " " + customerDetails.customerLandmark + " " + customerDetails.customerTown + ", " + customerDetails.customerPinCode;
        String message = orderEmailTemplate.getHeader(user.getFullName())
                + orderEmailTemplate.getOrderPlacedTemplate(cartItemsList, totalPrice, getFormattedDate(new Timestamp(System.currentTimeMillis()).toString()), customerAddress, savedOrder.getOrderId())
                + orderEmailTemplate.getFooter();
        cartItemsRepository.deleteCartItems(cart.getCardId());
        mailService.sendEmail(user.getEmail(), subject, message, bookList);
        return savedOrder.getOrderId();
    }

    @Override
    public List<Orders> fetchOrders(String token) {
        User user = userRepository.findById(jwtToken.verifyToken(token)).orElseThrow(() -> new UserException("User Not Found"));
        List<Orders> ordersPlacedByUser = orderRepository.findOrdersByUser_IdOrderByOrderPlacedDateDesc(user.getId());
        if (ordersPlacedByUser.isEmpty())
            throw new OrderException("There Is No Order Placed Yet");
        return ordersPlacedByUser;
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

    private String generateOrderId() {
        PageRequest pageRequest = PageRequest.of(0, 1, Sort.by("order_id").descending());
        Page<Orders> orders = orderRepository.fetchOrders(pageRequest);
        if (!orders.hasContent())
            return String.format("%08d", 1);
        String previousOrderId = orders.getContent()
                .get(0)
                .getOrderId()
                .substring(4)
                .replaceFirst("^0+(?!$)", "");
        return String.format("%08d", Integer.parseInt(previousOrderId) + 1);
    }
}
