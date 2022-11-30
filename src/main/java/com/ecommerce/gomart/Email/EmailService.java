package com.ecommerce.gomart.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @org.springframework.beans.factory.annotation.Value("oopsecommercewebsite@gmail.com") private String sender;

    public ResponseEntity<String> sendSimpleMail(Email email){
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(sender);
            message.setTo(email.getTo());
            message.setSubject(email.getSubject());
            message.setText(email.getBody());
            javaMailSender.send(message);
            return new ResponseEntity<String>("Mail sent successfully", HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<String>("Mail not sent: " + e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    public String generateRandomPassword(){
        String password = "";
        for(int i = 0; i < 16; i++){
            password += (char)(Math.random() * 26 + 97);
        }
        return password;
    }

    public String hashPassword(String password){

        String hashedPassword = encoder.encode(password);
        return hashedPassword;
    }
    
}
