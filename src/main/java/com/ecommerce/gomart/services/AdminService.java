package com.ecommerce.gomart.services;

import com.ecommerce.gomart.controllers.SendOrder;
import com.ecommerce.gomart.controllers.UserInfo;
import com.ecommerce.gomart.models.GomartUser;
import com.ecommerce.gomart.models.Manager;
import com.ecommerce.gomart.models.ManagerStatus;
import com.ecommerce.gomart.models.Order;
import com.ecommerce.gomart.models.Product;
import com.ecommerce.gomart.models.Role;
import com.ecommerce.gomart.repositories.GomartUserRepository;
import com.ecommerce.gomart.repositories.OrderRepository;
import com.ecommerce.gomart.repositories.ProductRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
            user.setManager(new Manager(true, ManagerStatus.Approved));
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
            user.setManager(new Manager(false, null));
            gomartUserRepository.save(user);
        }
        else{
            throw new RuntimeException("User is not an admin");
        }
    }

    public List<SendOrder> getOrdersOfCustomerInDateRange(Long adminId, Long userId, LocalDate startDate, LocalDate endDate){
        if(checkAdminStatus(adminId)){
            GomartUser user = gomartUserRepository.findById(userId).get();
            List<Order> orders = orderRepository.findByCustomerAndOrderDateBetween(user, startDate, endDate);
            List<SendOrder> send = orders.stream().map(order -> new SendOrder(order.getOrderTransactionId(), order.getProduct(), order.getQuantity(), order.getOrderDate())).collect(Collectors.toList());
            return send;
        }
        else{
            throw new RuntimeException("User is not an admin");
        }
        
    }

    public List<UserInfo> getCustomers(Long adminId){
        if(checkAdminStatus(adminId)){
            List<GomartUser> users = gomartUserRepository.findByRole(Role.CUSTOMER);
            List<UserInfo> send = users.stream().map(user -> new UserInfo(user.getUserId(), user.getFirstName(), user.getMiddleName(), user.getLastName(), user.getEmail(), user.getDob(), user.getAddress(), user.getPhoneNumber())).collect(Collectors.toList());
            return send;
        }
        else{
            throw new RuntimeException("User is not an admin");
        }
    }


    public List<UserInfo> getManagers(Long adminId){
        if(checkAdminStatus(adminId)){
            List<GomartUser> users = gomartUserRepository.findByRole(Role.MANAGER);
            List<UserInfo> send = users.stream().map(user -> new UserInfo(user.getUserId(), user.getFirstName(), user.getMiddleName(), user.getLastName(), user.getEmail(), user.getDob(), user.getAddress(), user.getPhoneNumber())).collect(Collectors.toList());
            return send;
        }
        else{
            throw new RuntimeException("User is not an admin");
        }
    }

    public List<UserInfo> getPendingManagers(Long adminId){
        if(checkAdminStatus(adminId)){
            List<GomartUser> managers = gomartUserRepository.findByManagerIsNotNull();
            managers.removeIf(manager -> manager.getManager().getManagerApplicationStatus() != ManagerStatus.Pending);
            List<UserInfo> send = managers.stream().map(user -> new UserInfo(user.getUserId(), user.getFirstName(), user.getMiddleName(), user.getLastName(), user.getEmail(), user.getDob(), user.getAddress(), user.getPhoneNumber())).collect(Collectors.toList());
            return send;
        }
        else{
            throw new RuntimeException("User is not an admin");
        }
    }

    private boolean checkAdminStatus(Long userId){
        Optional<GomartUser> gomartUser = gomartUserRepository.findById(userId);
        if(gomartUser.isPresent()){
            return gomartUser.get().getAdmin().isAdminPerms() && gomartUser.get().isLoginStatus();
        }
        return false;
    }

    public void saveImage(Long userId, Long productId, MultipartFile file) throws IOException {
        if(checkAdminStatus(userId)){
            Product product = productRepository.findById(productId).get();
            product.setImage(file.getBytes());
            productRepository.save(product);
        }
        else{
            throw new RuntimeException("User is not a admin");
        }
    }

    public List<Product> getAllProducts(Long userId){
        if(checkAdminStatus(userId)){
            return productRepository.findAll();
        }
        else{
            throw new RuntimeException("User is not a admin");
        }
    }

}
