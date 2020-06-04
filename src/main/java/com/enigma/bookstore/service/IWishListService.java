package com.enigma.bookstore.service;

import com.enigma.bookstore.model.User;
import com.enigma.bookstore.model.WishListItems;

import java.util.List;

public interface IWishListService {
    void createWishList(User user);

    String addToWishList(Integer bookId, String token);

    List<WishListItems> fetchWishList(String token);

    String deleteBookFromWishList(Integer wishListId, String token);
}
