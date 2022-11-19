package com.ecommerce.gomart.services;

import com.ecommerce.gomart.models.*;

import com.ecommerce.gomart.repositories.CartRepository;
import com.ecommerce.gomart.repositories.GomartUserRepository;
import com.ecommerce.gomart.repositories.OrderRepository;
import com.ecommerce.gomart.repositories.ProductRepository;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final GomartUserRepository gomartUserRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Autowired
    CustomerService(GomartUserRepository gomartUserRepository, OrderRepository orderRepository, CartRepository cartRepository, ProductRepository productRepository){
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.gomartUserRepository = gomartUserRepository;
        this.productRepository = productRepository;
    }

    public Product getProductById(Long id) {
        Optional<Product> p1 = productRepository.findById(id);
        return p1.get();
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductsByCategory(int category) {
        return productRepository.findByCategory(Category.values()[category]);
    }

    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    public List<Product> getProductsInCategoryByPriceRange(int category, double min, double max) {
        return productRepository.findByCategoryAndPriceBetween(Category.values()[category], min, max);
    }

    public GomartUser getUserInfo(Long userId){
        Optional<GomartUser> user = gomartUserRepository.findById(userId);
        return user.get();
    }

    public void updateUserInfo(GomartUser user){
        gomartUserRepository.save(user);
    }


    public void deleteUserInfo(Long userId){
        gomartUserRepository.deleteById(userId);
    }

    
     public void addToCart(Long userId, Long productId, Integer quantity){
         GomartUser user = gomartUserRepository.findById(userId).get();
         Product product = productRepository.findById(productId).get();
         Cart cart = new Cart().builder()
                 .customer(user)
                 .product(product)
                 .quantity(quantity).
                 build();
         cartRepository.save(cart);
     }

    public void removeFromCart(Long userId, Long productId){
        GomartUser user = gomartUserRepository.findById(userId).get();
        Product product = productRepository.findById(productId).get();
        cartRepository.deleteById(cartRepository.findByCustomerAndProduct(user, product).getEntryId());
    }

    public List<Cart> getCart(Long userId){
        GomartUser user = gomartUserRepository.findById(userId).get();
        return cartRepository.findByCustomer(user);
    
    }

    public void checkOutFromCart(Long userId){
        GomartUser user = gomartUserRepository.findById(userId).get();
        List<Cart> cartList = cartRepository.findByCustomer(user);
        for(Cart cart: cartList){
            Order order = new Order().builder()
                    .customer(cart.getCustomer())
                    .product(cart.getProduct())
                    .quantity(cart.getQuantity())
                    .orderDate(LocalDate.now())
                    .build();
            orderRepository.save(order);
        }
        cartRepository.deleteAll(cartList);
    }

    public void changeQuantityOfProductInCart(Long userId, Long productId, Integer quantity){
        GomartUser user = gomartUserRepository.findById(userId).get();
        Product product = productRepository.findById(productId).get();
        Cart cart = cartRepository.findByCustomerAndProduct(user, product);
        cart.setQuantity(quantity);
        cartRepository.save(cart);
    }
}
