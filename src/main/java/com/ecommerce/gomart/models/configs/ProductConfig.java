package com.ecommerce.gomart.models.configs;

import com.ecommerce.gomart.models.Category;
import com.ecommerce.gomart.models.Product;
import com.ecommerce.gomart.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductConfig {
@Bean
CommandLineRunner commandLineRunner(ProductRepository productRepository){
        return args -> {
        

        Product p1 = new Product().builder()
                .name("Apple")
                .price(55)
                .description("Fresh Apple")
                .quantity(10)
                .category(Category.Groceries)
                .offer(12.6)
                .build();

        productRepository.save(p1);

        Product p2 = new Product().builder()
                .name("Lays Chips")
                .price(20)
                .description("Big packet")
                .quantity(200)
                .category(Category.Food)
                .offer(0.0)
                .build();

        productRepository.save(p2);

        Product p3 = new Product().builder()
                .name("Nataraj Pencil Box")
                .price(100)
                .description("20 pencils")
                .quantity(100)
                .category(Category.Stationery)
                .offer(10.0)
                .build();

        productRepository.save(p3);

        Product p4 = new Product().builder()
                .name("Mixie")
                .price(199)
                .description("Mixie with 3 jars")
                .quantity(10)
                .category(Category.HomeAppliances)
                .offer(30.0)
                .build();   

        productRepository.save(p4);

        Product p5 = new Product().builder()
                        .name("Sri Sri Toothpaste")
                        .price(10)
                        .description("Fresh ayuevedic toothpaste")
                        .quantity(150)
                        .category(Category.Misc)
                        .offer(0.0)
                        .build();

        productRepository.save(p5);

        };



}
}
