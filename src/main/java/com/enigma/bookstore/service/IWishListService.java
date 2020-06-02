package com.enigma.bookstore.service;

import com.enigma.bookstore.model.User;

public interface IWishListService {
    void createWishList(User user);

    String addToWishList(Integer bookId, String token);
}
