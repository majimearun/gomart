package com.ecommerce.gomart.models.configs;

import com.ecommerce.gomart.models.*;
import com.ecommerce.gomart.repositories.GomartUserRepository;
import com.ecommerce.gomart.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;

@Configuration
public class ProductConfig {
@Bean
CommandLineRunner commandLineRunner(ProductRepository productRepository, GomartUserRepository gomartUserRepository) {
        return args -> {
        

        Product p1 = new Product().builder()
                .name("Namdhari's 1 dozen apples")
                .price(55)
                .description("Fresh Apple")
                .quantity(10)
                .category(Category.Groceries)
                .offer(12.6)
                .deliveryTime(1)
                .build();

        productRepository.save(p1);

        Product p2 = new Product().builder()
                .name("Lays Chips: Magic Masala")
                .price(20)
                .description("Big packet")
                .quantity(200)
                .category(Category.Food)
                .offer(0.0)
                .deliveryTime(1)
                .build();

        productRepository.save(p2);

        Product p3 = new Product().builder()
                .name("Nataraj Pencil Box")
                .price(100)
                .description("20 pencils")
                .quantity(100)
                .category(Category.Stationery)
                .offer(10.0)
                .deliveryTime(1)
                .build();

        productRepository.save(p3);

        Product p4 = new Product().builder()
                .name("Prestige Handy Mixie")
                .price(199)
                .description("Mixie with 3 jars")
                .quantity(10)
                .category(Category.HomeAppliances)
                .offer(30.0)
                .deliveryTime(1)
                .build();   

        productRepository.save(p4);

        Product p5 = new Product().builder()
                        .name("Sri Sri Toothpaste")
                        .price(10)
                        .description("Fresh ayuevedic toothpaste")
                        .quantity(150)
                        .category(Category.Misc)
                        .offer(0.0)
                        .deliveryTime(1)
                        .build();

        productRepository.save(p5);
        
        final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        GomartUser dummyAdmin = new GomartUser().builder()
                .firstName("Dummy")
                .lastName("Admin")
                .email("dummyAdmin@gmail.com")
                .password(encoder.encode("admin"))
                .role(Role.ADMIN)
                .dob(LocalDate.of(2000, 1, 1))
                .customer(new Customer(new Wallet(0)))
                .admin(new Admin(true))
                .manager(new Manager(true, null))
                .phoneNumber("9999999999")
                .address("Dummy Admin Address")
                .build();

                gomartUserRepository.save(dummyAdmin);
        };





}
}
