package com.ecommerce.gomart.controllers;
import com.ecommerce.gomart.models.Cart;
import com.ecommerce.gomart.models.GomartUser;
import com.ecommerce.gomart.models.Product;
import com.ecommerce.gomart.services.CustomerService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/user")
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

    @PutMapping(path = "/{userId}")
    public void updateUserInfo(@PathVariable Long userId, @RequestBody GomartUser user){
        customerService.updateUserInfo(user);
    }

    @PostMapping(path = "/addToCart")
    public void addToCart(@RequestBody GetCart getCart){
        customerService.addToCart(getCart.getProductId(), getCart.getUserId(), getCart.getQuantity());
    }

    @GetMapping(path = "/{userId}/cart")
    public @ResponseBody List<Cart> getCart(@PathVariable Long userId){
        return customerService.getCart(userId);
    }

    @PatchMapping(path = "/cart")
    public void updateCart(@RequestBody GetCart getCart){
        customerService.changeQuantityOfProductInCart(getCart.getProductId(), getCart.getUserId(), getCart.getQuantity());
    }

    @PostMapping(path = "/cart/checkout")
    public void checkout(@RequestBody GetCart getCart){
        customerService.checkOutFromCart(getCart.getUserId());
    }

    @PostMapping("wallet")
    public void addMoneyToWallet(@RequestBody GetWallet getWallet){
        customerService.topUpWallet(getWallet.getUserId(), getWallet.getAmount());
    }


}

