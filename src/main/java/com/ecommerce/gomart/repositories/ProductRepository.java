package com.ecommerce.gomart.repositories;

import com.ecommerce.gomart.models.Category;
import com.ecommerce.gomart.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(Category category);

    List<Product> findByName(String name);

    List<Product> findByCategoryAndPriceBetween(Category category, double min, double max);
    

}
