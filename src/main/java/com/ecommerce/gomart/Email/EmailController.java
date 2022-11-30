package com.ecommerce.gomart.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.gomart.GomartUser.GomartUser;
import com.ecommerce.gomart.GomartUser.GomartUserRepository;
import com.ecommerce.gomart.GomartUser.Customer.CustomerService;
import com.ecommerce.gomart.Stubs.GetEmail;

@RestController
@CrossOrigin
@RequestMapping(path = "/mail")
public class EmailController {
    private final EmailService emailService;
    private final GomartUserRepository gomartUserRepository;

    @Autowired
    public EmailController(EmailService emailService, GomartUserRepository gomartUserRepository) {
        this.emailService = emailService;
        this.gomartUserRepository = gomartUserRepository;
    }

    @PostMapping("/reset/password")
    public void sendMail(@RequestBody GetEmail getEmail) {
        GomartUser user = gomartUserRepository.findByEmail(getEmail.getEmail()).get();
        String password = emailService.generateRandomPassword();
        user.setPassword(emailService.hashPassword(password));
        gomartUserRepository.save(user);
        Email email = Email.builder()
                .to(user.getEmail())
                .subject("Password Reset")
                .body("Your password has been reset to: " + password + "\nPlease change your password after logging in.")
                .build();
        emailService.sendSimpleMail(email);
        
    }



}