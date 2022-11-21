package com.ecommerce.gomart.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.gomart.models.*;
import com.ecommerce.gomart.repositories.*;

@Service
public class AdminService {
    private final GomartUserRepository gomartUserRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Autowired
    AdminService(GomartUserRepository gomartUserRepository, OrderRepository orderRepository, ProductRepository productRepository){
        this.orderRepository = orderRepository;
        this.gomartUserRepository = gomartUserRepository;
        this.productRepository = productRepository;
    }

    public void addProduct(Product product){
        productRepository.save(product);
    }

    public void updateProduct(Product product){
        productRepository.save(product);
    }

    public void deleteProduct(Long id){
        productRepository.deleteById(id);
    }

    public void giveManagerAccess(Long userId){
        GomartUser user = gomartUserRepository.findById(userId).get();
        user.setManager(new Manager(true));
        gomartUserRepository.save(user);
    }
}
