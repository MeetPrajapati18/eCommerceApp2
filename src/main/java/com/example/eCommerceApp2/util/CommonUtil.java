package com.example.eCommerceApp2.util;

import com.example.eCommerceApp2.model.ProductOrder;
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

    public void sendMailForProductOrder(ProductOrder order, String status) throws MessagingException, UnsupportedEncodingException {
        String orderMessage = "<p>[[name]]</p><br><p>Your order is [[orderStatus]].</p>" +
                "<p><b>Product Details : </b></p>" +
                "<p>Name : [[Product Name]]</p>" +
                "<p>Category : [[Category]]</p>" +
                "<p>Quantity : [[Quantity]]</p>" +
                "<p>Price : [[product Price]]</p>" +
                "<p>Total Price : [[Total Price]]</p>" +
                "<p>Payment Type : [[Payment Type]]</p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("ace180505@gmail.com", "MP E-Commerce Store");
        helper.setTo(order.getOrderAddress().getEmail());

        double totalPrice = order.getPrice() * order.getQuantity();

        // Replace placeholders with current order details
        orderMessage = orderMessage.replace("[[name]]", order.getOrderAddress().getFirstname());
        orderMessage = orderMessage.replace("[[orderStatus]]", status);
        orderMessage = orderMessage.replace("[[Product Name]]", order.getProduct().getTitle());
        orderMessage = orderMessage.replace("[[Category]]", order.getProduct().getCategory());
        orderMessage = orderMessage.replace("[[Quantity]]", order.getQuantity().toString());
        orderMessage = orderMessage.replace("[[product Price]]", order.getPrice().toString());
        orderMessage = orderMessage.replace("[[Total Price]]", String.valueOf(totalPrice));
        orderMessage = orderMessage.replace("[[Payment Type]]", order.getPaymentType());

        helper.setSubject("Product Order Information");
        helper.setText(orderMessage, true);
        mailSender.send(message);
    }

}
