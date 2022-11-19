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
                    .price(199)
                    .description("Fresh Apple")
                    .quantity(10)
                    .category(Category.Food)
                    .offer(12.6)
                    .build();

            productRepository.save(p1);

            Product p2 = new Product().builder()
                    .name("Mango")
                    .price(199)
                    .description("Fresh Mango")
                    .quantity(10)
                    .category(Category.Food)
                    .offer(12.6)
                    .build();

            productRepository.save(p2);

            Product p3 = new Product().builder()
                    .name("Banana")
                    .price(199)
                    .description("Fresh Banana")
                    .quantity(10)
                    .category(Category.Food)
                    .offer(12.6)
                    .build();

            productRepository.save(p3);

            Product p4 = new Product().builder()
                    .name("Orange")
                    .price(199)
                    .description("Fresh Orange")
                    .quantity(10)
                    .category(Category.Food)
                    .offer(12.6)
                    .build();   

            productRepository.save(p4);

        };



    }
}
