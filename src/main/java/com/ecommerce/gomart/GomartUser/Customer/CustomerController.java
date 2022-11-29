package com.ecommerce.gomart.GomartUser.Customer;

import com.ecommerce.gomart.GomartUser.GomartUser;
import com.ecommerce.gomart.Product.Product;
import com.ecommerce.gomart.Stubs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "/user")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService){
        this.customerService = customerService;
    }

    @GetMapping(path = "products")
    public @ResponseBody Iterable<Product> getProducts(){
        return customerService.getProducts();
    }

    @GetMapping(path = "products/{id}")
    public @ResponseBody Product getProductById(@PathVariable Long id){
        return customerService.getProductById(id);
    }

    @GetMapping(path = "products/category/{category}")
    public @ResponseBody Iterable<Product> getProductsByCategory(@PathVariable int category){
        return customerService.getProductsByCategory(category);
    }

    @GetMapping(path = "products/name/{name}")
    public @ResponseBody Iterable<Product> getProductsByName(@PathVariable String name){
        return customerService.getProductsByName(name);
    }

    @PostMapping(path = "products/category/priceRange")
    public @ResponseBody Iterable<Product> getProductsInCategoryByPriceRange( @RequestBody GetProduct getProduct){

        return customerService.getProductsInCategoryByPriceRange(getProduct.getCategory(), getProduct.getMin(), getProduct.getMax());
    }

    @GetMapping(path = "/{userId}")
    public @ResponseBody GomartUser getUserInfo(@PathVariable Long userId){
        return customerService.getUserInfo(userId);
    }

    @PostMapping(path = "update/{userId}")
    public void updateUserInfo(@PathVariable Long userId, @RequestBody UserInfo userInfo){
        customerService.updateUserInfo(userInfo.getUserId(), userInfo.getFirstName(), userInfo.getMiddleName(), userInfo.getLastName(),  userInfo.getDob(),userInfo.getEmail(), userInfo.getAddress(), userInfo.getPhone());
    }

    @PostMapping(path = "/addToCart")
    public void addToCart(@RequestBody GetCart getCart){
        customerService.addToCart(getCart.getUserId(), getCart.getProductId(), getCart.getQuantity());
    }

    @GetMapping(path = "/{userId}/cart")
    public @ResponseBody List<SendCart> getCart(@PathVariable Long userId){
        return customerService.getCart(userId);
    }

    @PostMapping(path = "update/cart")
    public void updateCart(@RequestBody GetCart getCart){
        customerService.changeQuantityOfProductInCart(getCart.getUserId(), getCart.getProductId(), getCart.getQuantity());
    }

    @PostMapping(path = "/cart")
    public void deleteFromCart(@RequestBody GetCart getCart){
        customerService.removeFromCart(getCart.getUserId(), getCart.getProductId());
    }

    @PostMapping(path = "/cart/checkout")
    public void checkout(@RequestBody GetCart getCart){
        customerService.checkOutFromCart(getCart.getUserId());
    }

    @PostMapping("wallet")
    public void addMoneyToWallet(@RequestBody GetWallet getWallet){
        customerService.topUpWallet(getWallet.getUserId(), getWallet.getAmount());
    }

    @GetMapping(path = "/{userId}/orders")
    public @ResponseBody List<SendOrder> getOrders(@PathVariable Long userId){
        return customerService.getOrders(userId);
    }

    @PostMapping(path = "/orders")
    public @ResponseBody List<SendOrder> getOrdersByDate(@RequestBody GetOrder getOrder){
        return customerService.getOrdersByOrderDate(getOrder.getUserId(), getOrder.getStartDate());
    }

    @PostMapping(path = "/orders/dateRange")
    public @ResponseBody List<SendOrder> getOrdersByDateRange(@RequestBody GetOrder getOrder){
        return customerService.getOrdersByOrderDateRange(getOrder.getUserId(), getOrder.getStartDate(), getOrder.getEndDate());
    }

    @PostMapping(path = "/login")
    public Long login(@RequestBody Login login){
        return customerService.login(login.getEmail(), login.getPassword());
    }

    @PostMapping(path = "/logout")
    public void logout(@RequestBody Login login){
        customerService.logout(login.getUserId());
    }

    @PostMapping(path = "/signup")
    public void register(@RequestBody Signup signup){
        customerService.signUp(signup.getPassword(), signup.getFirstName(), signup.getLastName(), signup.getDob(), signup.getEmail(), signup.getAmount(), signup.getAddress(), signup.getPhone());
    }

    @PostMapping(path = "/apply/manager")
    public void applyForManager(@RequestBody Login login){
        customerService.applyAsManager(login.getUserId());
    }

    @PostMapping(path = "/deleteAccount")
    public void deleteAccount(@RequestBody Login login){
        customerService.deleteUserInfo(login.getUserId());
    }

    @PostMapping(path = "/changePassword")
    public void changePassword(@RequestBody ChangePassword changePassword){
        customerService.resetPassword(changePassword.getUserId(), changePassword.getOldPassword(), changePassword.getNewPassword());
    }


}

