package pjj.ibf2021project.server.services;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService{
    
    @Autowired
    private JavaMailSender emailSender;

    private Logger logger = Logger.getLogger(EmailService.class.getName());
    
    public void sendEmail(String resp) {

        String emailFrom = "shadowysupercoder69420@gmail.com";
        String emailTo = "jian_jun3@hotmail.com";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailFrom);
        message.setTo(emailTo);
        message.setSubject("Crypto Twitter Notification");
        message.setText(resp);
        emailSender.send(message);
        logger.log(Level.INFO, "Sent email from " + emailFrom + " to " + emailTo);
    }
}
