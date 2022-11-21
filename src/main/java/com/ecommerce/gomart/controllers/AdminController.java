package com.ecommerce.gomart.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.gomart.models.GomartUser;
import com.ecommerce.gomart.models.Order;
import com.ecommerce.gomart.models.Product;
import com.ecommerce.gomart.services.AdminService;

@RestController
@RequestMapping(path = "/api/v1/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService){
        this.adminService = adminService;
    }

    @PostMapping(path = "/addManager")
    public void addManager(@RequestBody Long userId){
        adminService.giveManagerAccess(userId);
    }

    @PostMapping(path = "/removeManager")
    public void removeManager(@RequestBody Long userId){
        adminService.removeManagerAccess(userId);
    }

    @PostMapping(path = "/addProduct")
    public void addProduct(@RequestBody Product product){
        adminService.addProduct(product);
    }

    @PostMapping(path = "/updateProduct")
    public void updateProduct(@RequestBody Product product){
        adminService.updateProduct(product);
    }

    @PostMapping(path = "/deleteProduct")
    public void deleteProduct(@RequestBody Long id){
        adminService.deleteProduct(id);
    }

    @PostMapping(path = "/report")
    public List<Order> generateReport(@RequestBody GetOrder getOrder){
        return adminService.getOrdersOfCustomerInDateRange(getOrder.getUserId(), getOrder.getStartDate(), getOrder.getEndDate());
    }

    @GetMapping(path = "/customers")
    public List<GomartUser> getCustomers(){
        return adminService.getCustomers();
    }

    @GetMapping(path = "/products")
    public List<Product> getProducts(){
        return adminService.getProducts();
    }

    @GetMapping(path = "/managers")
    public List<GomartUser> getManagers(){
        return adminService.getManagers();
    }

    @GetMapping(path = "/products/{name}")
    public List<Product> getProductByName(@RequestBody String name){
        return adminService.getProductsByName(name);
    }
}
