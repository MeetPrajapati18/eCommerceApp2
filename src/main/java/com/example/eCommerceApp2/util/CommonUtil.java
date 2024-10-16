package com.example.eCommerceApp2.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
public class CommonUtil {

    @Autowired
    private JavaMailSender mailSender; // Remove static here

    public static Boolean sendMail(JavaMailSender mailSender, String url, String recipientEmail) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("ace180505@gmail.com", "MP E-Commerce Store");
        helper.setTo(recipientEmail);

        String content = "<p>Hello,</p>" +
                "<p>You have requested to reset your password.</p>" +
                "<p>Click the link below to change your password:</p>" +
                "<p><a href=\"" + url + "\">Change my password</a></p>";

        helper.setSubject("Password Reset Request");
        helper.setText(content, true);
        mailSender.send(message);
        return true;
    }

    public static String generateURL(HttpServletRequest request) {
        String siteUrl = request.getRequestURL().toString();
        return siteUrl.replace(request.getServletPath(), "");
    }
}
