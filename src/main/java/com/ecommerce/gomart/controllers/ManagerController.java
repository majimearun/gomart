package com.ecommerce.gomart.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.gomart.models.Product;
import com.ecommerce.gomart.services.ManagerService;

@RestController
@RequestMapping(path = "/api/v1/manager")
public class ManagerController {
    private final ManagerService managerService;

    @Autowired
    public ManagerController(ManagerService managerService){
        this.managerService = managerService;
    }

    @PostMapping(path = "/addProduct")
    public void addProduct(@RequestBody Product product){
        managerService.addProduct(product);
    }

    @PostMapping(path = "/updateProduct")
    public void updateProduct(@RequestBody Product product){
        managerService.updateProduct(product);
    }

    @PostMapping(path = "/deleteProduct")

    public void deleteProduct(@RequestBody Long id){
        managerService.deleteProduct(id);
    }
}
