package com.enigma.bookstore.service.implementation;

import com.enigma.bookstore.model.User;
import com.enigma.bookstore.service.ISendNotification;
import com.enigma.bookstore.util.IMailService;
import com.enigma.bookstore.util.implementation.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


@Service
public class SendNotification implements ISendNotification {

    @Autowired
    IMailService mailService;
    private static final Logger logger = LoggerFactory.getLogger(MailService.class);

    ScheduledExecutorService quickService = Executors.newScheduledThreadPool(20);

    public void notifyUsers(List<User> subscriberList) {
        subscriberList.forEach(user ->
                quickService.submit(() -> {
                            try {
                                mailService.sendEmail(user.getEmail(), "Book Is Now Available", "Come To Our WebSite");
                            } catch (Exception e) {
                                logger.error("Exception occur while send a mail : ", e);
                            }
                        }
                )
        );
    }
}
