package com.ecommerce.gomart.GomartUser.Admin;

import com.ecommerce.gomart.Product.Product;
import com.ecommerce.gomart.Stubs.GetInfo;
import com.ecommerce.gomart.Stubs.GetOrder;
import com.ecommerce.gomart.Stubs.SendOrder;
import com.ecommerce.gomart.Stubs.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "/admin")
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

    @PostMapping(path = "/report")
    public List<SendOrder> generateReport(@RequestBody GetOrder getOrder){
        return adminService.getOrdersOfCustomerInDateRange(getOrder.getSenderId(), getOrder.getUserId(), getOrder.getStartDate(), getOrder.getEndDate());
    }

    @PostMapping(path = "/customers")
    public List<UserInfo> getCustomers(@RequestBody GetInfo getInfo){
        return adminService.getCustomers(getInfo.getSenderId());
    }

    @PostMapping(path = "/managers")
    public List<UserInfo> getManagers(@RequestBody GetInfo getInfo){
        return adminService.getManagers(getInfo.getSenderId());
    }

    @PostMapping(path = "/managers/pending")
    public List<UserInfo> getPendingManagers(@RequestBody GetInfo getInfo){
        return adminService.getPendingManagers(getInfo.getSenderId());
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

    @PostMapping(path = "/saveImage")
    public void saveImage(@RequestParam("file") MultipartFile file, @RequestParam("productId") Long productId, @RequestParam("userId") Long userId) throws IOException {
        adminService.saveImage(userId, productId, file);
    }

}
