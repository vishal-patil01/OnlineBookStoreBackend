package com.enigma.bookstore.service.implementation;

import com.enigma.bookstore.dto.CustomerDTO;
import com.enigma.bookstore.dto.FeedbackDTO;
import com.enigma.bookstore.enums.AddressType;
import com.enigma.bookstore.exception.CustomerException;
import com.enigma.bookstore.exception.UserException;
import com.enigma.bookstore.model.Book;
import com.enigma.bookstore.model.Customer;
import com.enigma.bookstore.model.Feedback;
import com.enigma.bookstore.model.User;
import com.enigma.bookstore.repository.IBookRepository;
import com.enigma.bookstore.repository.ICustomerRepository;
import com.enigma.bookstore.repository.IFeedbackRepository;
import com.enigma.bookstore.repository.IUserRepository;
import com.enigma.bookstore.service.ICustomerService;
import com.enigma.bookstore.util.ITokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService implements ICustomerService {

    @Autowired
    private ICustomerRepository customerRepository;
    @Autowired
    private IBookRepository bookRepository;
    @Autowired
    private IFeedbackRepository feedbackRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private ITokenGenerator jwtToken;

    @Override
    public String addCustomerDetails(CustomerDTO customerDTO, String token) {
        int userId = jwtToken.verifyToken(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException("User Not Exist"));
        Customer customerDetails = new Customer(customerDTO, user);
        List<Customer> customersList = customerRepository.findByUserIdAndAndCustomerAddressType(userId, customerDTO.customerAddressType);
        if (!customersList.isEmpty())
            return updateCustomerDetails(customerDTO, customersList.get(0));
        customerRepository.save(customerDetails);
        return "Customer Details Saved Successfully";
    }

    private String updateCustomerDetails(CustomerDTO customerDTO, Customer customer) {
        customer.updateCustomer(customerDTO);
        customerRepository.save(customer);
        return "Customer Details Updated Successfully";
    }

    @Override
    public Customer fetchCustomerDetails(AddressType addressType, String token) {
        int userId = jwtToken.verifyToken(token);
        List<Customer> customerList = customerRepository.findByUserIdAndAndCustomerAddressType(userId, addressType);
        if (customerList.isEmpty())
            throw new CustomerException("There is No CustomerData Available");
        return customerList.get(0);
    }

    @Override
    public String addFeedback(String token, FeedbackDTO feedbackDto) {
        boolean isUserFeedbackPresent = false;
        int userId = jwtToken.verifyToken(token);
        userRepository.findById(userId).orElseThrow(() -> new UserException("User Not Found"));
        String isbn = feedbackDto.isbNumber;
        Optional<Book> book1 = bookRepository.findByIsbnNumber(isbn);
        Book book = book1.get();
        int bookid = book.getId();
        List<Integer> feedbackIds = feedbackRepository.getFeedbackIds(bookid);
        if (feedbackIds.size() > 0) {
            for (Integer feedbackId : feedbackIds) {
                int userFeedbackId = feedbackRepository.getUserFeedbackId(feedbackId);
                if (userId == userFeedbackId) {
                    isUserFeedbackPresent = true;
                    break;
                }
            }
        }
        if (!isUserFeedbackPresent) {
            String feedbackMessage = feedbackDto.feedbackMessage;
            int rating = feedbackDto.rating;
            Feedback userFeedback = new Feedback(userId, rating, feedbackMessage, book);
            feedbackRepository.save(userFeedback);
            return "Thank you For your Feedback ";
        }
        throw new UserException("You had submitted feedback previously");
    }

    @Override
    public List<FeedbackDTO> getAllFeedback(String isbn) {
        Optional<Book> book = bookRepository.findByIsbnNumber(isbn);
        Book bookData = book.get();
        Integer id = bookData.getId();
        List<Integer> feedbackIds = feedbackRepository.getFeedbackIds(id);
        List<FeedbackDTO> feedbackList = new ArrayList<>();
        for (Integer feedbackId : feedbackIds) {
            Optional<Feedback> feedback = feedbackRepository.findById(feedbackId);
            Feedback feedback1 = feedback.get();
            int userId = feedback1.userId;
            Optional<User> userById = userRepository.findById(userId);
            String userName = userById.get().getFullName();
            feedbackList.add(new FeedbackDTO(feedback1.rating, feedback1.feedbackMessage, "", userName));
        }
        return feedbackList;
    }

    @Override
    public List<FeedbackDTO> getUserFeedback(Integer bookId, String token) {
        int userId = jwtToken.verifyToken(token);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserException("User Not Found"));
        List<FeedbackDTO> feedbackList = new ArrayList<>();
        String userName = user.getFullName();
        Integer feedbackId = feedbackRepository.getFeedbackId(userId, bookId);
        if (feedbackId == null) {
            throw new UserException("Feedback not found for this user");
        } else {
            Optional<Feedback> feedback = feedbackRepository.findById(feedbackId);
            Feedback feedbackDetails = feedback.get();
            feedbackList.add(new FeedbackDTO(feedbackDetails.rating, feedbackDetails.feedbackMessage, "", userName));
            return feedbackList;
        }
    }
}
