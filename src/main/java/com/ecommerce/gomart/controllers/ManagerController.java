package com.ecommerce.gomart.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
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
    public void addProduct(@RequestBody ProductInfo productInfo){
        managerService.addProduct(productInfo.getUserId(), productInfo.getProduct());
    }

    @PostMapping(path = "/updateProduct")
    public void updateProduct(@RequestBody ProductInfo productInfo){
        managerService.updateProduct(productInfo.getUserId(), productInfo.getProduct());
    }

    @PostMapping(path = "/deleteProduct")
    public void deleteProduct(@RequestBody ProductInfo productInfo){
        managerService.deleteProduct(productInfo.getUserId(), productInfo.getProduct().getProductId());
    }

    @PostMapping(path = "/saveImage")
    public void saveImage(@RequestParam("file") MultipartFile file, @RequestParam("productId") Long productId, @RequestParam("userId") Long userId) throws IOException {
        managerService.saveImage(userId, productId, file);
    }
}
