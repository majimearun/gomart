package com.ecommerce.gomart.repositories;

import com.ecommerce.gomart.models.Cart;
import com.ecommerce.gomart.models.GomartUser;
import com.ecommerce.gomart.models.Product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    public List<Cart> findByCustomer(GomartUser user);
    public Cart findByCustomerAndProduct(GomartUser user, Product product);
    
}
