package com.ecommerce.gomart.Cart;

import com.ecommerce.gomart.GomartUser.GomartUser;
import com.ecommerce.gomart.Product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    public List<Cart> findByCustomer(GomartUser user);
    public Optional<Cart> findByCustomerAndProduct(GomartUser user, Product product);
    
}
