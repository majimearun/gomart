package com.ecommerce.gomart.services;

import com.ecommerce.gomart.models.Admin;
import com.ecommerce.gomart.models.GomartUser;
import com.ecommerce.gomart.models.Manager;
import com.ecommerce.gomart.models.ManagerStatus;
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


    public List<GomartUser> getManagers(Long adminId){
        if(checkAdminStatus(adminId)){
            return gomartUserRepository.findByManagerIsNotNull();
        }
        else{
            throw new RuntimeException("User is not an admin");
        }
    }

    public List<GomartUser> getPendingManagers(Long adminId){
        if(checkAdminStatus(adminId)){
            List<GomartUser> managers = gomartUserRepository.findByManagerIsNotNull();
            managers.removeIf(manager -> manager.getManager().getManagerApplicationStatus() != ManagerStatus.Pending);
            return managers;
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

}
