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
                    .build();

            productRepository.save(p1);

        };



    }
}
