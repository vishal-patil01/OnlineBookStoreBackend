package com.enigma.bookstore.service.implementation;

import com.enigma.bookstore.dto.BookDTO;
import com.enigma.bookstore.dto.EmailTemplateDTO;
import com.enigma.bookstore.dto.UserLoginDTO;
import com.enigma.bookstore.enums.UserRole;
import com.enigma.bookstore.exception.BookException;
import com.enigma.bookstore.exception.UserException;
import com.enigma.bookstore.model.Book;
import com.enigma.bookstore.model.CartItems;
import com.enigma.bookstore.model.User;
import com.enigma.bookstore.model.WishListItems;
import com.enigma.bookstore.properties.ApplicationProperties;
import com.enigma.bookstore.rabbitmq.producer.NotificationSender;
import com.enigma.bookstore.repository.IBookRepository;
import com.enigma.bookstore.repository.ICartItemsRepository;
import com.enigma.bookstore.repository.IUserRepository;
import com.enigma.bookstore.repository.IWishListItemsRepository;
import com.enigma.bookstore.service.IAdminService;
import com.enigma.bookstore.util.EmailTemplateGenerator;
import com.enigma.bookstore.util.ITokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService implements IAdminService {

    @Autowired
    private IBookRepository bookRepository;

    @Autowired
    private IWishListItemsRepository wishListItemsRepository;

    @Autowired
    private ICartItemsRepository cartItemsRepository;

    @Autowired
    EmailTemplateGenerator emailTemplateGenerator;

    @Autowired
    NotificationSender notificationSender;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private ITokenGenerator jwtToken;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Override
    public String addBook(BookDTO bookDTO) {
        Book book = new Book(bookDTO);
        boolean isIsbnNumberPresent = bookRepository.findByIsbnNumber(bookDTO.isbnNumber).isPresent();
        boolean isBookNamePresent = bookRepository.findByBookNameAndAuthorName(bookDTO.bookName, bookDTO.authorName).isPresent();
        if (isIsbnNumberPresent) {
            throw new BookException("ISBN Number is already exists.");
        } else if (isBookNamePresent) {
            throw new BookException("Book Name and Author Name is already exists.");
        }
        bookRepository.save(book);
        return "Book Added successfully.";
    }

    @Override
    public String uploadImage(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename()).replace(" ","").toLowerCase();
        String fileBasePath = System.getProperty("user.dir") + applicationProperties.getUploadDir();
        if (!(fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png"))) {
            throw new BookException("Only Image Files Can Be Uploaded");
        }
        Path path = Paths.get(fileBasePath + fileName);
        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("bookstore/image/")
                .path(fileName)
                .toUriString();
    }

    @Override
    public String updateBook(BookDTO bookDTO, Integer bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookException("Book Not Found during Update Operation"));
        if (book.getNoOfCopies() <= 0 && bookDTO.noOfCopies >= book.getNoOfCopies()) {
            List<Book> bookList = new ArrayList<>();
            bookList.add(book);
            getSubscribersList(book).forEach(user -> notificationSender
                    .addToSubscriberQueue(new EmailTemplateDTO(user.getEmail(),
                            "Your Requested Product Is Now Available To Purchase",
                            emailTemplateGenerator.getHeader(user.getFullName()) + emailTemplateGenerator.getBookAvailableInStockTemplate(book) + emailTemplateGenerator.getFooter(),
                            bookList)));
        }
        book.updateBook(bookDTO);
        bookRepository.save(book);
        return "Book Updated successfully.";
    }

    @Override
    public String deleteBook(Integer bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookException("Book Not Found during Delete Operation"));
        List<WishListItems> wishListItemsList = wishListItemsRepository.findAllByBookId(bookId);
        List<CartItems> cartItemsList = cartItemsRepository.findAllByBookId(bookId);
        if (!cartItemsList.isEmpty() || !wishListItemsList.isEmpty())
            throw new BookException("Book Can Not Be Deleted. It May Be Added In WishList Or Cart");
        bookRepository.delete(book);
        return "Book Deleted Successfully";
    }

    @Override
    public String adminLogin(UserLoginDTO userLoginDTO) {
        User user = getUser(userLoginDTO.email);
        if (user.getUserRole() == UserRole.ADMIN) {
            boolean isPasswordMatched = bCryptPasswordEncoder.matches(userLoginDTO.password, user.getPassword());
            if (isPasswordMatched) {
                Date expirationTime = getExpirationTime(Calendar.DAY_OF_YEAR, 1);
                return jwtToken.generateToken(user.getId(), expirationTime);
            }
            throw new UserException("Enter Valid Password");
        }
        throw new UserException("Your Dont Have Admin Privilege");
    }

    private Date getExpirationTime(Integer timePeriod, Integer value) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(timePeriod, value);
        return calendar.getTime();
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("Account With This Email Address Not Exist"));
    }

    private List<User> getSubscribersList(Book book) {
        return wishListItemsRepository.findAllByBookId(book.getId())
                .stream()
                .map(wishListItems -> wishListItems.getWishList().getUser())
                .collect(Collectors.toList());
    }
}