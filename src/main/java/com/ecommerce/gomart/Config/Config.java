package com.ecommerce.gomart.Config;

import com.ecommerce.gomart.GomartUser.Admin.Admin;
import com.ecommerce.gomart.GomartUser.Customer.Customer;
import com.ecommerce.gomart.GomartUser.Customer.Wallet;
import com.ecommerce.gomart.GomartUser.GomartUser;
import com.ecommerce.gomart.GomartUser.GomartUserRepository;
import com.ecommerce.gomart.GomartUser.Manager.Manager;
import com.ecommerce.gomart.GomartUser.Role;
import com.ecommerce.gomart.Product.Category;
import com.ecommerce.gomart.Product.Product;
import com.ecommerce.gomart.Product.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;

@Configuration
public class Config {
@Bean
CommandLineRunner commandLineRunner(ProductRepository productRepository, GomartUserRepository gomartUserRepository) {
        return args -> {
        

        Product p1 = new Product().builder()
                .name("Namdharis' Fresh Apples")
                .price(550)
                .description("1 dozen apples")
                .quantity(10)
                .category(Category.Groceries)
                .offer(12.0)
                .deliveryTime(1)
                .build();

        productRepository.save(p1);

        Product p2 = new Product().builder()
                .name("Lays Chips: Magic Masala")
                .price(20)
                .description("Medium sized packet of chips")
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
                .name("Prestige Induction Cooktop")
                .price(199)
                .description("1 induction cooktop")
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
                .firstName("Arunachala")
                .lastName("A M")
                .email("arunachala0619@gmail.com")
                .password(encoder.encode("admin"))
                .role(Role.ADMIN)
                .dob(LocalDate.of(2003, 6, 24))
                .customer(new Customer(new Wallet(0)))
                .admin(new Admin(true))
                .manager(new Manager(true, null))
                .phoneNumber("9999999999")
                .address("Bangalore South")
                .build();

                gomartUserRepository.save(dummyAdmin);
        };





}
}