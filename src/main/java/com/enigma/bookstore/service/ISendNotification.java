package com.enigma.bookstore.service;

import com.enigma.bookstore.model.User;

import java.util.List;


public interface ISendNotification {
    void notifyUsers(List<User> subscriberList);
}
