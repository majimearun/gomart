package com.ecommerce.gomart.controllers;

import com.ecommerce.gomart.services.ManagerService;
import com.ecommerce.gomart.stub.GetInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping(path = "/manager")
public class ManagerController {
    private final ManagerService managerService;

    @Autowired
    public ManagerController(ManagerService managerService){
        this.managerService = managerService;
    }

    @PostMapping(path = "/addProduct")
    public void addProduct(@RequestBody GetInfo getInfo){
        managerService.addProduct(getInfo.getSenderId(),getInfo.getProduct());
    }

    @PostMapping(path = "/updateProduct")
    public void updateProduct(@RequestBody GetInfo getInfo){
        managerService.updateProduct(getInfo.getSenderId(),getInfo.getProduct());
    }

    @PostMapping(path = "/deleteProduct")
    public void deleteProduct(@RequestBody GetInfo getInfo){
        managerService.deleteProduct(getInfo.getSenderId(), getInfo.getProduct().getProductId());
    }

    @PostMapping(path = "/saveImage")
    public void saveImage(@RequestParam("file") MultipartFile file, @RequestParam("productId") Long productId, @RequestParam("userId") Long userId) throws IOException {
        managerService.saveImage(userId, productId, file);
    }
}
