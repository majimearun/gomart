package com.ecommerce.gomart.services;

import com.ecommerce.gomart.models.*;
import com.ecommerce.gomart.repositories.CartRepository;
import com.ecommerce.gomart.repositories.GomartUserRepository;
import com.ecommerce.gomart.repositories.OrderRepository;
import com.ecommerce.gomart.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public void signUp(String password, String firstName, String lastName, LocalDate dob, String email, double amount){
        Wallet wallet = new Wallet().builder()
                .amount(amount)
                .build();
        Customer customer = new Customer(
                wallet
        );
        GomartUser gomartUser = new GomartUser().builder()
                .password(password)
                .loginStatus(false)
                .firstName(firstName)
                .lastName(lastName)
                .dob(dob)
                .email(email)
                .customer(customer)
                .admin(new Admin(false))
                .manager(new Manager(false))
                .role(Role.CUSTOMER)
                .build();
        gomartUserRepository.save(gomartUser);
    }

    public void login(Long userId, String password){
        Optional<GomartUser> gomartUser = gomartUserRepository.findById(userId);
        if(gomartUser.isPresent()){
            if(gomartUser.get().getPassword().equals(password)){
                gomartUser.get().setLoginStatus(true);
                gomartUserRepository.save(gomartUser.get());
                ResponseEntity.ok().body("Logged In");
            }
            else{
                ResponseEntity.status(null).body("Incorrect Password");
            }
        }
    }

    public void logout(Long userId){
        Optional<GomartUser> gomartUser = gomartUserRepository.findById(userId);
        if(gomartUser.isPresent()){
            gomartUser.get().setLoginStatus(false);
            gomartUserRepository.save(gomartUser.get());
            ResponseEntity.ok().body("Logged Out");
        }
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
        return productRepository.findByFuzzyName(name);
    }

    public List<Product> getProductsInCategoryByPriceRange(int category, double min, double max) {
        return productRepository.findByCategoryAndPriceBetween(Category.values()[category], min, max);
    }

    public GomartUser getUserInfo(Long userId){
        if(checkIfUserLoggedIn(userId)){
            Optional<GomartUser> user = gomartUserRepository.findById(userId);
            return user.get();
        }
        else{
            ResponseEntity.status(null).body("User not logged in");
            return null;
        }
        
    }

    public List<Cart> getCart(Long userId){
        if(checkIfUserLoggedIn(userId)){
            GomartUser user = gomartUserRepository.findById(userId).get();
            System.out.println(user);
            List<Cart> carts =  cartRepository.findByCustomer(user);
            System.out.println(carts);
            return carts;
    
        }
        else{
            ResponseEntity.status(null).body("User not logged in");
            return null;
        }
        
    }

    public List<Order> getOrders(Long userId){
        if(checkIfUserLoggedIn(userId)){
            GomartUser user = gomartUserRepository.findById(userId).get();
            return orderRepository.findByCustomer(user);
        }
        else{
            ResponseEntity.status(null).body("User not logged in");
            return null;
        }
    }

    public List<Order> getOrdersByOrderDate(Long userId, LocalDate date){
        if(checkIfUserLoggedIn(userId)){
            GomartUser user = gomartUserRepository.findById(userId).get();
            return orderRepository.findByCustomerAndOrderDate(user, date);
        }
        else{
            ResponseEntity.status(null).body("User not logged in");
            return null;
        }
    }

    public List<Order> getOrdersByOrderDateRange(Long userId, LocalDate startDate, LocalDate endDate){
        if(checkIfUserLoggedIn(userId)){
            GomartUser user = gomartUserRepository.findById(userId).get();
            return orderRepository.findByCustomerAndOrderDateBetween(user, startDate, endDate);
        }
        else{
            ResponseEntity.status(null).body("User not logged in");
            return null;
        }
    }

    public void updateUserInfo(GomartUser user){
        if(checkIfUserLoggedIn(user.getUserId())){
            gomartUserRepository.save(user);
        }
        else{
            ResponseEntity.status(null).body("User not logged in");
        }

    }


    public void deleteUserInfo(Long userId){
        if(checkIfUserLoggedIn(userId)){
            gomartUserRepository.deleteById(userId);
        }
        else{
            ResponseEntity.status(null).body("User not logged in");
        }
    }

    
    public void addToCart(Long userId, Long productId, Integer quantity){
        if(checkIfUserLoggedIn(userId)){
            GomartUser user = gomartUserRepository.findById(userId).get();
            Product product = productRepository.findById(productId).get();
            Cart cart = new Cart().builder()
                    .customer(user)
                    .product(product)
                    .quantity(quantity).
                    build();
            cartRepository.save(cart);
        }
        else{
            ResponseEntity.status(null).body("User not logged in");
        }
    }

    public void removeFromCart(Long userId, Long productId){
        if(checkIfUserLoggedIn(userId)){
            GomartUser user = gomartUserRepository.findById(userId).get();
            Product product = productRepository.findById(productId).get();
            cartRepository.deleteById(cartRepository.findByCustomerAndProduct(user, product).getEntryId());
        }
        else{
            ResponseEntity.status(null).body("User not logged in");
        }
        
    }

    public void checkOutFromCart(Long userId){
        if(checkIfUserLoggedIn(userId)){
            GomartUser user = gomartUserRepository.findById(userId).get();
            List<Cart> cartList = cartRepository.findByCustomer(user);
            double total = 0;
            for(Cart cart: cartList){
                total += (100 - cart.getProduct().getOffer())/100 * cart.getProduct().getPrice() * cart.getQuantity();
            }
            if(total > user.getCustomer().getWallet().getAmount()){
                throw new RuntimeException("Insufficient Balance");
            }
            else{
                for(Cart cart: cartList){
                    Order order = new Order().builder()
                            .customer(cart.getCustomer())
                            .product(cart.getProduct())
                            .quantity(cart.getQuantity())
                            .orderDate(LocalDate.now())
                            .build();
                    orderRepository.save(order);
                    cartRepository.deleteById(cart.getEntryId());
                    // decrease quantity of product
                    Product product = cart.getProduct();
                    product.setQuantity(product.getQuantity() - cart.getQuantity());
                    productRepository.save(product);
                }
                user.getCustomer().getWallet().setAmount(user.getCustomer().getWallet().getAmount() - total);
                gomartUserRepository.save(user);
            }
        }
        else{
            ResponseEntity.status(null).body("User not logged in");
        }
        
        
    }

    public void topUpWallet(Long userId, double amount){
        if(checkIfUserLoggedIn(userId)){
            if(amount > 0){
                GomartUser user = gomartUserRepository.findById(userId).get();
                user.getCustomer().getWallet().setAmount(user.getCustomer().getWallet().getAmount() + amount);
                gomartUserRepository.save(user);
            }
            else{
                throw new RuntimeException("Invalid Amount");
            }
        }
        else{
            ResponseEntity.status(null).body("User not logged in");
        }
    }

    public void changeQuantityOfProductInCart(Long userId, Long productId, Integer quantity){

        if(checkIfUserLoggedIn(userId)){
            GomartUser user = gomartUserRepository.findById(userId).get();
            Product product = productRepository.findById(productId).get();
            Cart cart = cartRepository.findByCustomerAndProduct(user, product);
            cart.setQuantity(quantity);
            cartRepository.save(cart);
        }
        else{
            ResponseEntity.status(null).body("User not logged in");
        }
    }

    private boolean checkIfUserLoggedIn(Long userId){
        GomartUser user = gomartUserRepository.findById(userId).get();
        return user.isLoginStatus();
    }


}
