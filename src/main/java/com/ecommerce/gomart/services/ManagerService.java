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

    public void signUp(String password, String firstName, String middleName, String lastName, String email){
        Manager manager = new Manager(true);
        Admin admin = new Admin(false);
        GomartUser gomartUser = new GomartUser().builder()
                .password(password)
                .loginStatus(false)
                .firstName(firstName)
                .middleName(middleName)
                .lastName(lastName)
                .email(email)
                .manager(manager)
                .admin(admin)
                .role(Role.MANAGER)
                .build();
        gomartUserRepository.save(gomartUser);
    }

    public void login(Long userId, String password){
        Optional<GomartUser> gomartUser = gomartUserRepository.findById(userId);
        if(gomartUser.isPresent()){
            if(gomartUser.get().getPassword().equals(password)){
                gomartUser.get().setLoginStatus(true);
                gomartUserRepository.save(gomartUser.get());
                ResponseEntity.ok().body("Logged In");
            }
            else{
                ResponseEntity.status(null).body("Incorrect Password");
            }
        }
    }

    public void logout(Long userId){
        Optional<GomartUser> gomartUser = gomartUserRepository.findById(userId);
        if(gomartUser.isPresent()){
            gomartUser.get().setLoginStatus(false);
            gomartUserRepository.save(gomartUser.get());
            ResponseEntity.ok().body("Logged Out");
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
