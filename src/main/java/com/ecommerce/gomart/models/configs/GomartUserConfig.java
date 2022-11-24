package com.ecommerce.gomart.models.configs;

import com.ecommerce.gomart.models.*;
import com.ecommerce.gomart.repositories.GomartUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.Month;

@Configuration
public class GomartUserConfig {
@Bean
CommandLineRunner commandLineRunner2(GomartUserRepository gomartUserRepository){
        return args -> {
        Wallet wallet = new Wallet().builder()
                .amount(10000)
                .build();
        Customer c1 = new Customer(
                wallet
        );

        Manager m1 = new Manager(false);
        Admin a1 = new Admin(false);
        GomartUser u1 = new GomartUser().builder()
                .password("password")
                .loginStatus(false)
                .firstName("A")
                .middleName("B")
                .lastName("C")
                .dob(LocalDate.of(1998, Month.JUNE, 24))
                .email("abc@gmail.com")
                .customer(c1)
                .manager(m1)
                .admin(a1)
                .role(Role.CUSTOMER)
                .build();

        gomartUserRepository.save(u1);

        Wallet wallet2 = new Wallet().builder()
                .amount(0)
                .build(); 

        Customer c2 = new Customer(
                wallet2
        );

        Manager m2 = new Manager(true);
        Admin a2 = new Admin(false);
        GomartUser u2 = new GomartUser().builder()
                .password("password")
                .loginStatus(false)
                .firstName("D")
                .middleName("E")
                .lastName("F")
                .dob(LocalDate.of(1998, Month.JUNE, 24))
                .email("def@gmail.com")
                .customer(c2)
                .manager(m2)
                .admin(a2)
                .role(Role.MANAGER)
                .build();
        gomartUserRepository.save(u2);

        Wallet wallet3 = new Wallet().builder()
                        .amount(0)
                        .build();

        Customer c3 = new Customer(
                        wallet3
        );

        Manager m3 = new Manager(true);
        Admin a3 = new Admin(true);

        GomartUser u3 = new GomartUser().builder()
                .password("password")
                .loginStatus(false)
                .firstName("G")
                .middleName("H")
                .lastName("I")
                .dob(LocalDate.of(1998, Month.JUNE, 24))
                .email("ghi@gmail.com")
                .customer(c3)
                .manager(m3)
                .admin(a3)
                .role(Role.ADMIN)
                .build();

        gomartUserRepository.save(u3);

        };
}
}
