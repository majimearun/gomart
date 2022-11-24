package com.ecommerce.gomart.services;

import com.ecommerce.gomart.models.Admin;
import com.ecommerce.gomart.models.GomartUser;
import com.ecommerce.gomart.models.Manager;
import com.ecommerce.gomart.models.Order;
import com.ecommerce.gomart.models.Product;
import com.ecommerce.gomart.models.Role;
import com.ecommerce.gomart.repositories.GomartUserRepository;
import com.ecommerce.gomart.repositories.OrderRepository;
import com.ecommerce.gomart.repositories.ProductRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

    public void removeManagerAccess(Long userId){
        GomartUser user = gomartUserRepository.findById(userId).get();
        user.setManager(new Manager(false));
        gomartUserRepository.save(user);
    }

    public List<Order> getOrdersOfCustomerInDateRange(Long userId, LocalDate startDate, LocalDate endDate){
        GomartUser user = gomartUserRepository.findById(userId).get();
        return orderRepository.findByCustomerAndOrderDateBetween(user, startDate, endDate);
    }

    public List<GomartUser> getCustomers(){
        return gomartUserRepository.findAll();
    }

    public List<Product> getProducts(){
        return productRepository.findAll();
    }

    public List<GomartUser> getManagers(){
        return gomartUserRepository.findByManagerIsNotNull();
    }

    public List<Product> getProductsByName(String name) {
        return productRepository.findByFuzzyName(name);
    }

    public void signUp(String password, String firstName, String middleName, String lastName, String email, Role role){
        Admin admin = new Admin(true);
        Manager manager = new Manager(false);
        GomartUser gomartUser = new GomartUser().builder()
                .password(password)
                .loginStatus(false)
                .firstName(firstName)
                .middleName(middleName)
                .lastName(lastName)
                .email(email)
                .admin(admin)
                .manager(manager)
                .role(role)
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

}
