package com.revature.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailUtil {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Sends an email
     * @param to The email address to send the email to
     * @param subject The email's subject
     * @param body The body of the email
     */
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("virtualpubliclib@gmail.com");
        message.setTo(to);
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);
    }

    /**
     * Generates a welcome email for new users
     * @param firstName The new user's first name
     * @param lastName The new user's last name
     * @return A welcome message for the user
     */
    public String generateWelcomeEmail(String firstName, String lastName) {
        String result = "";
        result += "Dear " + firstName + " " + lastName + ",\n\n";
        result += "Welcome to the Virtual Public Library! Here you will find an " +
                "assortment of eBooks to browse and check out.\n\n";
        result += "What are you waiting for? Check out your first book today!\n\n";
        result += "Yours,\n";
        result += "The Virtual Public Library Team";
        return result;
    }

}
