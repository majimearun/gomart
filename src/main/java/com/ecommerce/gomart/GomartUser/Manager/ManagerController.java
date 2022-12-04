package com.ecommerce.gomart.GomartUser.Manager;

import com.ecommerce.gomart.Product.Product;
import com.ecommerce.gomart.Stubs.GetInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "https://gomart.vercel.app/")
@RequestMapping(path = "/manager")
public class ManagerController {
    private final ManagerService managerService;

    @Autowired
    public ManagerController(ManagerService managerService){
        this.managerService = managerService;
    }

    @PostMapping(path = "/addProduct")
    public Long addProduct(@RequestBody GetInfo getInfo){
        return managerService.addProduct(getInfo.getSenderId(),getInfo.getProduct());
    }

    @PostMapping(path = "/updateProduct")
    public Long updateProduct(@RequestBody GetInfo getInfo){
        return managerService.updateProduct(getInfo.getSenderId(),getInfo.getProduct());
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
