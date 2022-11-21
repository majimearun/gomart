package com.ecommerce.gomart.services;

import com.ecommerce.gomart.models.GomartUser;
import com.ecommerce.gomart.models.Manager;
import com.ecommerce.gomart.models.Order;
import com.ecommerce.gomart.models.Product;
import com.ecommerce.gomart.repositories.GomartUserRepository;
import com.ecommerce.gomart.repositories.OrderRepository;
import com.ecommerce.gomart.repositories.ProductRepository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

}
