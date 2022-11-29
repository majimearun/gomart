package com.ecommerce.gomart.repositories;

import com.ecommerce.gomart.models.Category;
import com.ecommerce.gomart.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(Category category);

    @Query(value = "SELECT *, LEVENSHTEIN(product_name, :name) FROM gomart_products WHERE LEVENSHTEIN(product_name, :name) <= 10 ORDER BY LEVENSHTEIN(product_name, :name) ASC LIMIT 10", nativeQuery = true)
    List<Product> findByFuzzyName(String name);

    List<Product> findByCategoryAndPriceBetween(Category category, double min, double max);
    

}
