package com.ecommerce.gomart.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
