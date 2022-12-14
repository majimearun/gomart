package com.ecommerce.gomart.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(Category category);

    List<Product> findByCategoryAndPriceBetween(Category category, double min, double max);

    List<Product> findByNameIgnoreCaseContaining(String name);

    List<Product> findByDescriptionIgnoreCaseContaining(String description);

    Product findByName(String name);
    
    

}
