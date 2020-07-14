package com.enigma.bookstore.service.implementation;

import com.enigma.bookstore.exception.BookException;
import com.enigma.bookstore.exception.UserException;
import com.enigma.bookstore.exception.WishListException;
import com.enigma.bookstore.exception.WishListItemsException;
import com.enigma.bookstore.model.Book;
import com.enigma.bookstore.model.User;
import com.enigma.bookstore.model.WishList;
import com.enigma.bookstore.model.WishListItems;
import com.enigma.bookstore.repository.IBookRepository;
import com.enigma.bookstore.repository.IUserRepository;
import com.enigma.bookstore.repository.IWishListItemsRepository;
import com.enigma.bookstore.repository.IWishListRepository;
import com.enigma.bookstore.service.IWishListService;
import com.enigma.bookstore.util.ITokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishListService implements IWishListService {

    @Autowired
    IBookRepository bookRepository;
    @Autowired
    private IWishListItemsRepository wishListItemsRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IWishListRepository wishListRepository;
    @Autowired
    private ITokenGenerator jwtToken;

    @Override
    public void createWishList(User user) {
        WishList wishList = new WishList();
        wishList.setUser(user);
        wishListRepository.save(wishList);
    }

    @Override
    public String addToWishList(Integer bookId, String token) {
        WishList wishList = checkUserAndWishListIsExists(token);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookException("Book Not Found"));
        List<WishListItems> wishListItemsList = wishListItemsRepository.findByBookIdAndWishListWishId(book.getId(), wishList.getWishId());
        if (wishListItemsList.isEmpty()) {
            WishListItems wishListItems = new WishListItems(book, wishList);
            wishListItemsRepository.save(wishListItems);
            return "Book Added To Wish List Successfully";
        }
        throw new WishListItemsException("Book Already Exists In WishList");
    }

    @Override
    public List<WishListItems> fetchWishList(String token) {
        WishList wishList = checkUserAndWishListIsExists(token);
        List<WishListItems> wishListItemsList = wishListItemsRepository.findAllByWishListWishId(wishList.getWishId());
        if (wishListItemsList.isEmpty())
            throw new WishListItemsException("There Is No Books In WishList");
        return wishListItemsList;
    }

    @Override
    public String deleteBookFromWishList(Integer bookId, String token) {
        WishList wishList = checkUserAndWishListIsExists(token);
        List<WishListItems> wishListItems = wishListItemsRepository.findByBookIdAndWishListWishId(bookId, wishList.getWishId());
        if (wishListItems.isEmpty())
            throw new WishListItemsException("No Such Book In Wish List");
        wishListItemsRepository.deleteWishItems(bookId, wishList.getWishId());
        return "Book Removed From WishList";
    }

    private WishList checkUserAndWishListIsExists(String token) {
        int userId = jwtToken.verifyToken(token);
        userRepository.findById(userId)
                .orElseThrow(() -> new UserException("User Not Exist"));
        return wishListRepository.findByUserId(userId)
                .orElseThrow(() -> new WishListException("WishList Id Not Found"));
    }
}