package com.ecommerce.gomart.models.configs;

import java.time.LocalDate;
import java.time.Month;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ecommerce.gomart.models.Admin;
import com.ecommerce.gomart.models.Customer;
import com.ecommerce.gomart.models.GomartUser;
import com.ecommerce.gomart.models.Manager;
import com.ecommerce.gomart.models.Wallet;
import com.ecommerce.gomart.repositories.GomartUserRepository;

@Configuration
public class GomartUserConfig {
    @Bean
    CommandLineRunner commandLineRunner2(GomartUserRepository gomartUserRepository){
        return args -> {
            Wallet wallet = new Wallet().builder()
                    .amount(1000)
                    .build();
            Customer c1 = new Customer(
                    wallet
            );

            Manager m1 = new Manager(true);
            Admin a1 = new Admin(true);
            GomartUser u1 = new GomartUser().builder()
                    .firstName("A")
                    .middleName("B")
                    .lastName("C")
                    .dob(LocalDate.of(1998, Month.JUNE, 24))
                    .email("abc@gmail.com")
                    .customer(c1)
                    .manager(m1)
                    .admin(a1)
                    .build();

            gomartUserRepository.save(u1);

            Wallet wallet2 = new Wallet().builder()
                    .amount(1000)
                    .build(); 

            Customer c2 = new Customer(
                    wallet2
            );

            Manager m2 = new Manager(true);
            Admin a2 = new Admin(true);
            GomartUser u2 = new GomartUser().builder()
                    .firstName("D")
                    .middleName("E")
                    .lastName("F")
                    .dob(LocalDate.of(1998, Month.JUNE, 24))
                    .email("def@gmail.com")
                    .customer(c2)
                    .manager(m2)
                    .admin(a2)
                    .build();
            gomartUserRepository.save(u2);

        };
    }
}
