package com.enigma.bookstore.service.implementation;

import com.enigma.bookstore.dto.BookDTO;
import com.enigma.bookstore.exception.BookException;
import com.enigma.bookstore.model.Book;
import com.enigma.bookstore.model.CartItems;
import com.enigma.bookstore.model.WishListItems;
import com.enigma.bookstore.properties.ApplicationProperties;
import com.enigma.bookstore.repository.IBookRepository;
import com.enigma.bookstore.repository.ICartItemsRepository;
import com.enigma.bookstore.repository.IWishListItemsRepository;
import com.enigma.bookstore.service.IAdminService;
import com.enigma.bookstore.util.IMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class AdminService implements IAdminService {

    @Autowired
    private IBookRepository bookRepository;

    @Autowired
    private IWishListItemsRepository wishListItemsRepository;

    @Autowired
    private ICartItemsRepository cartItemsRepository;

    @Autowired
    IMailService mailService;

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
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
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
}
