package com.enigma.bookstore.util.implementation;

import com.enigma.bookstore.exception.BookException;
import com.enigma.bookstore.properties.ApplicationProperties;
import com.enigma.bookstore.util.EmailTemplateGenerator;
import com.enigma.bookstore.util.IMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class MailService implements IMailService {

    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    private ApplicationProperties applicationProperties;
    @Autowired
    EmailTemplateGenerator orderEmailTemplate;

    public final void sendEmail(String email, String subject, String message) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(message, true);
            helper.addInline("bookStoreLogo", new File(System.getProperty("user.dir") + applicationProperties.getUploadDir() + "bookStoreLogo.png"));
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new BookException(e.getMessage());
        }
    }
}
