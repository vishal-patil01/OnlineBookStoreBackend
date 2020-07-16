package com.enigma.bookstore.util.implementation;

import com.enigma.bookstore.exception.BookException;
import com.enigma.bookstore.model.Book;
import com.enigma.bookstore.properties.ApplicationProperties;
import com.enigma.bookstore.util.EmailTemplateGenerator;
import com.enigma.bookstore.util.IMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;

@Service
public class MailService implements IMailService {

    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    private ApplicationProperties applicationProperties;
    @Autowired
    EmailTemplateGenerator orderEmailTemplate;

    @Override
    public void sendEmail(String email, String subject, String message) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        getMimeMessageHelper(email, subject, message, mimeMessage);
        javaMailSender.send(mimeMessage);
    }

    @Override
    public void sendEmail(String email, String subject, String message, List<Book> attachments) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = getMimeMessageHelper(email, subject, message, mimeMessage);
        for (Book book : attachments) {
            try {
                String fileName = book.getBookImageSrc().substring(book.getBookImageSrc().lastIndexOf('/') + 1);
                mimeMessageHelper.addInline(fileName, new File(System.getProperty("user.dir") + applicationProperties.getUploadDir() + fileName));
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
        javaMailSender.send(mimeMessage);
    }

    private MimeMessageHelper getMimeMessageHelper(String email, String subject, String message, MimeMessage mimeMessage) {
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(message, true);
            helper.addInline("bookStoreLogo", new File(System.getProperty("user.dir") + applicationProperties.getUploadDir() + "bookStoreLogo.png"));
            return helper;
        } catch (Exception e) {
            throw new BookException(e.getMessage());
        }
    }
}
