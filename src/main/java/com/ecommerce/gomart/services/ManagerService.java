package com.ecommerce.gomart.services;

import com.ecommerce.gomart.models.*;
import com.ecommerce.gomart.repositories.GomartUserRepository;
import com.ecommerce.gomart.repositories.OrderRepository;
import com.ecommerce.gomart.repositories.ProductRepository;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ManagerService {
    private final GomartUserRepository gomartUserRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Autowired
    ManagerService(GomartUserRepository gomartUserRepository, OrderRepository orderRepository, ProductRepository productRepository){
        this.orderRepository = orderRepository;
        this.gomartUserRepository = gomartUserRepository;
        this.productRepository = productRepository;
    }

    public void addProduct(Long userId, Product product){
        if(checkManagerStatus(userId)){
            productRepository.save(product);
        }
        else{
            throw new RuntimeException("User is not a manager");
        }
    }

    public void updateProduct(Long userId, Product product){
        if(checkManagerStatus(userId)){
            productRepository.save(product);
        }
        else{
            throw new RuntimeException("User is not a manager");
        }
    }

    public void deleteProduct(Long userId, Long id){
        if(checkManagerStatus(userId)){
            productRepository.deleteById(id);
        }
        else{
            throw new RuntimeException("User is not a manager");
        }
    }

    public void saveImage(Long userId, Long productId, MultipartFile file) throws IOException {
        if(checkManagerStatus(userId)){
            Product product = productRepository.findById(productId).get();
            product.setImage(file.getBytes());
            productRepository.save(product);
        }
        else{
            throw new RuntimeException("User is not a manager");
        }
    }


    private boolean checkManagerStatus(Long userId){
        Optional<GomartUser> gomartUser = gomartUserRepository.findById(userId);
        if(gomartUser.isPresent()){
            return gomartUser.get().getManager().isManagerPerms() && gomartUser.get().isLoginStatus();
        }
        return false;
    }


}
