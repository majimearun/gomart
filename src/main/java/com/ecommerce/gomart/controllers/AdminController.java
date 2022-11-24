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
    public void addManager(@RequestBody GetInfo getInfo){
        adminService.giveManagerAccess(getInfo.getSenderId(),getInfo.getUserId());
    }

    @PostMapping(path = "/removeManager")
    public void removeManager(@RequestBody GetInfo getInfo){
        adminService.removeManagerAccess(getInfo.getSenderId(),getInfo.getUserId());
    }

    @PostMapping(path = "/addProduct")
    public void addProduct(@RequestBody GetInfo getInfo){
        adminService.addProduct(getInfo.getSenderId(),getInfo.getProduct());
    }

    @PostMapping(path = "/updateProduct")
    public void updateProduct(@RequestBody GetInfo getInfo){
        adminService.updateProduct(getInfo.getSenderId(),getInfo.getProduct());
    }

    @PostMapping(path = "/deleteProduct")
    public void deleteProduct(@RequestBody GetInfo getInfo){
        adminService.deleteProduct(getInfo.getSenderId(), getInfo.getProduct().getProductId());
    }

    @PostMapping(path = "/report")
    public List<Order> generateReport(@RequestBody GetOrder getOrder){
        return adminService.getOrdersOfCustomerInDateRange(getOrder.getSenderId(), getOrder.getUserId(), getOrder.getStartDate(), getOrder.getEndDate());
    }

    @GetMapping(path = "/customers")
    public List<GomartUser> getCustomers(@RequestBody GetInfo getInfo){
        return adminService.getCustomers(getInfo.getSenderId());
    }

    @GetMapping(path = "/products")
    public List<Product> getProducts(@RequestBody GetInfo getInfo){
        return adminService.getProducts(getInfo.getSenderId());
    }

    @GetMapping(path = "/managers")
    public List<GomartUser> getManagers(@RequestBody GetInfo getInfo){
        return adminService.getManagers(getInfo.getSenderId());
    }

    @GetMapping(path = "/products/{name}")
    public List<Product> getProductByName(@RequestBody GetInfo getInfo){
        return adminService.getProductsByName(getInfo.getSenderId(), getInfo.getName());
    }
}
