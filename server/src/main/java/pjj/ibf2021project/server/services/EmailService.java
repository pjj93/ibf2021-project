package pjj.ibf2021project.server.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import pjj.ibf2021project.server.models.Tweet;

@Service
public class EmailService{
    
    @Autowired
    private JavaMailSender emailSender;

    private Logger logger = Logger.getLogger(EmailService.class.getName());
    
    public void sendEmail(Tweet tweet, List<String> listEmailTo) {

        String[] emailTos = listEmailTo.toArray(String[]::new);
        String emailFrom = System.getenv("GMAIL_USERNAME");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailFrom);
        message.setBcc(emailTos);

        
        message.setSubject("Coin Twitter Notification: " + tweet.getTag());
        message.setText(tweet.getUsername() + " tweeted:\n\n" + tweet.getText());
        emailSender.send(message);
    }
}
