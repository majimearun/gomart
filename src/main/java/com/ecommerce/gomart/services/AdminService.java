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

    public void addProduct(Long adminId, Product product){
        if(checkAdminStatus(adminId)){
            productRepository.save(product);
        }
        else{
            throw new RuntimeException("User is not an admin");
        }
    }

    public void updateProduct(Long adminId, Product product){
        if(checkAdminStatus(adminId)){
            productRepository.save(product);
        }
        else{
            throw new RuntimeException("User is not an admin");
        }
    }

    public void deleteProduct(Long adminId, Long id){
        if(checkAdminStatus(adminId)){
            productRepository.deleteById(id);
        }
        else{
            throw new RuntimeException("User is not an admin");
        }
    }

    public void giveManagerAccess(Long adminId, Long userId){
        if(checkAdminStatus(adminId)){
            GomartUser user = gomartUserRepository.findById(userId).get();
            user.setRole(Role.MANAGER);
            user.setManager(new Manager(true));
            gomartUserRepository.save(user);
        }
        else{
            throw new RuntimeException("User is not an admin");
        }
    }

    public void removeManagerAccess(Long adminId, Long userId){
        if(checkAdminStatus(adminId)){
            GomartUser user = gomartUserRepository.findById(userId).get();
            user.setRole(Role.CUSTOMER);
            user.setManager(new Manager(false));
            gomartUserRepository.save(user);
        }
        else{
            throw new RuntimeException("User is not an admin");
        }
    }

    public List<Order> getOrdersOfCustomerInDateRange(Long adminId, Long userId, LocalDate startDate, LocalDate endDate){
        if(checkAdminStatus(adminId)){
            GomartUser user = gomartUserRepository.findById(userId).get();
            return orderRepository.findByCustomerAndOrderDateBetween(user, startDate, endDate);
        }
        else{
            throw new RuntimeException("User is not an admin");
        }
        
    }

    public List<GomartUser> getCustomers(Long adminId){
        if(checkAdminStatus(adminId)){
            return gomartUserRepository.findByRole(Role.CUSTOMER);
        }
        else{
            throw new RuntimeException("User is not an admin");
        }
    }

    public List<Product> getProducts(Long adminId){
        if(checkAdminStatus(adminId)){
            return productRepository.findAll();
        }
        else{
            throw new RuntimeException("User is not an admin");
        }
    }

    public List<GomartUser> getManagers(Long adminId){
        if(checkAdminStatus(adminId)){
            return gomartUserRepository.findByManagerIsNotNull();
        }
        else{
            throw new RuntimeException("User is not an admin");
        }
    }

    public List<Product> getProductsByName(Long admnId, String name) {
        if(checkAdminStatus(admnId)){
            return productRepository.findByFuzzyName(name);
        }
        else{
            throw new RuntimeException("User is not an admin");
        }
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

    private boolean checkAdminStatus(Long userId){
        Optional<GomartUser> gomartUser = gomartUserRepository.findById(userId);
        if(gomartUser.isPresent()){
            return gomartUser.get().getAdmin().isAdminPerms() && gomartUser.get().isLoginStatus();
        }
        return false;
    }

}
