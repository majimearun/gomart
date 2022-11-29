package com.ecommerce.gomart.GomartUser.Customer;

import com.ecommerce.gomart.Cart.Cart;
import com.ecommerce.gomart.Cart.CartRepository;
import com.ecommerce.gomart.GomartUser.Admin.Admin;
import com.ecommerce.gomart.GomartUser.GomartUser;
import com.ecommerce.gomart.GomartUser.GomartUserRepository;
import com.ecommerce.gomart.GomartUser.Manager.Manager;
import com.ecommerce.gomart.GomartUser.Manager.ManagerStatus;
import com.ecommerce.gomart.GomartUser.Role;
import com.ecommerce.gomart.Order.Order;
import com.ecommerce.gomart.Order.OrderRepository;
import com.ecommerce.gomart.Product.Category;
import com.ecommerce.gomart.Product.Product;
import com.ecommerce.gomart.Product.ProductRepository;
import com.ecommerce.gomart.Stubs.SendCart;
import com.ecommerce.gomart.Stubs.SendOrder;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final GomartUserRepository gomartUserRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    CustomerService(GomartUserRepository gomartUserRepository, OrderRepository orderRepository, CartRepository cartRepository, ProductRepository productRepository){
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.gomartUserRepository = gomartUserRepository;
        this.productRepository = productRepository;
    }

    public void signUp(String password, String firstName, String lastName, LocalDate dob, String email, double amount, String address, String phone){
        Wallet wallet = new Wallet().builder()
                .amount(amount)
                .build();
        Customer customer = new Customer(
                wallet
        );
        GomartUser gomartUser = new GomartUser().builder()
                .password(hashPassword(password))
                .loginStatus(false)
                .firstName(firstName)
                .lastName(lastName)
                .dob(dob)
                .email(email)
                .phoneNumber(phone)
                .address(address)
                .customer(customer)
                .admin(new Admin(false))
                .manager(new Manager(false, null))
                .role(Role.CUSTOMER)
                .build();
        gomartUserRepository.save(gomartUser);
    }

    public Long login(String email, String password){
        Optional<GomartUser> gomartUser = gomartUserRepository.findByEmail(email);
        if(gomartUser.isPresent()){
            if(encoder.matches(password, gomartUser.get().getPassword())){
                gomartUser.get().setLoginStatus(true);
                gomartUserRepository.save(gomartUser.get());
                return gomartUser.get().getUserId();
            }
            else{
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect Password");
                return null;
            }
        }
        else{
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
            return null;
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
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not logged in");
            return null;
        }
        
    }

    public List<SendCart> getCart(Long userId){
        if(checkIfUserLoggedIn(userId)){
            GomartUser user = gomartUserRepository.findById(userId).get();
            List<Cart> carts =  cartRepository.findByCustomer(user);
            List<SendCart> send = carts.stream().map(cart -> new SendCart(cart.getProduct(), cart.getQuantity())).collect(Collectors.toList());
            return send;
    
        }
        else{
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not logged in");
            return null;
        }
        
    }

    public List<SendOrder> getOrders(Long userId){
        if(checkIfUserLoggedIn(userId)){
            GomartUser user = gomartUserRepository.findById(userId).get();
            List<Order> orders = orderRepository.findByCustomer(user);
            List<SendOrder> send = orders.stream().map(order -> new SendOrder(order.getOrderTransactionId(),order.getProduct(), order.getQuantity(), order.getOrderDate())).collect(Collectors.toList());
            return send;
        }
        else{
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not logged in");
            return null;
        }
    }

    public List<SendOrder> getOrdersByOrderDate(Long userId, LocalDate date){
        if(checkIfUserLoggedIn(userId)){
            GomartUser user = gomartUserRepository.findById(userId).get();
            List<Order> orders = orderRepository.findByCustomerAndOrderDate(user, date);
            List<SendOrder> send = orders.stream().map(order -> new SendOrder(order.getOrderTransactionId(),order.getProduct(), order.getQuantity(), order.getOrderDate())).collect(Collectors.toList());
            return send;
        }
        else{
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not logged in");
            return null;
        }
    }

    public List<SendOrder> getOrdersByOrderDateRange(Long userId, LocalDate startDate, LocalDate endDate){
        if(checkIfUserLoggedIn(userId)){
            GomartUser user = gomartUserRepository.findById(userId).get();
            List<Order> orders = orderRepository.findByCustomerAndOrderDateBetween(user, startDate, endDate);
            List<SendOrder> send = orders.stream().map(order -> new SendOrder(order.getOrderTransactionId(),order.getProduct(), order.getQuantity(), order.getOrderDate())).collect(Collectors.toList());
            return send;
        
        }
        else{
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not logged in");
            return null;
        }
    }

    public void updateUserInfo(Long userId, String firstName, String middleName, String lastName, LocalDate dob, String email, String address, String phone){
        if(checkIfUserLoggedIn(userId)){
            GomartUser user = gomartUserRepository.findById(userId).get();
            user.setFirstName(firstName);
            user.setMiddleName(middleName);
            user.setLastName(lastName);
            user.setDob(dob);
            user.setEmail(email);
            user.setAddress(address);
            user.setPhoneNumber(phone);
            gomartUserRepository.save(user);
        }
        else{
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not logged in");
        }

    }


    public void deleteUserInfo(Long userId){
        if(checkIfUserLoggedIn(userId)){
            gomartUserRepository.deleteById(userId);
        }
        else{
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not logged in");
        }
    }

    
    public void addToCart(Long userId, Long productId, Integer quantity){
        if(checkIfUserLoggedIn(userId)){
            GomartUser user = gomartUserRepository.findById(userId).get();
            Product product = productRepository.findById(productId).get();
            // check if product is already in cart
            Optional<Cart> cart = cartRepository.findByCustomerAndProduct(user, product);
            if(cart.isPresent()){
                Cart newCart = cart.get();
                if(newCart.getQuantity() + quantity > product.getQuantity()){
                    newCart.setQuantity(product.getQuantity());
                    ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not enough quantity. already maximum");
                    cartRepository.save(newCart);
                }
                else{
                    newCart.setQuantity(newCart.getQuantity() + quantity);
                    cartRepository.save(newCart);
                }
            }
            else{
                Cart newCart = new Cart();
                newCart.setCustomer(user);
                newCart.setProduct(product);
                if(quantity > product.getQuantity()){
                    newCart.setQuantity(product.getQuantity());
                    ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not enough quantity. already maximum");
                }
                else{
                    newCart.setQuantity(quantity);
                }
                cartRepository.save(newCart);
            }
            
        }
        else{
            ResponseEntity.status(null).body("User not logged in");
        }
    }

    public void removeFromCart(Long userId, Long productId){
        if(checkIfUserLoggedIn(userId)){
            GomartUser user = gomartUserRepository.findById(userId).get();
            Product product = productRepository.findById(productId).get();
            cartRepository.deleteById(cartRepository.findByCustomerAndProduct(user, product).get().getEntryId());
        }
        else{
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not logged in");
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
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not logged in");
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
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not logged in");
        }
    }

    public void changeQuantityOfProductInCart(Long userId, Long productId, Integer quantity){

        if(checkIfUserLoggedIn(userId)){
            GomartUser user = gomartUserRepository.findById(userId).get();
            Product product = productRepository.findById(productId).get();
            Cart cart = cartRepository.findByCustomerAndProduct(user, product).get();
            cart.setQuantity(quantity);
            cartRepository.save(cart);
        }
        else{
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not logged in");
        }
    }

    private boolean checkIfUserLoggedIn(Long userId){
        GomartUser user = gomartUserRepository.findById(userId).get();
        return user.isLoginStatus();
    }

    public void applyAsManager(Long userId){
        if(checkIfUserLoggedIn(userId)){
            GomartUser user = gomartUserRepository.findById(userId).get();
            user.getManager().setManagerApplicationStatus(ManagerStatus.Pending);
            gomartUserRepository.save(user);
        }
        else{
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not logged in");
        }
    }

    public double getWalletBalance(Long userId){
        if(checkIfUserLoggedIn(userId)){
            GomartUser user = gomartUserRepository.findById(userId).get();
            return user.getCustomer().getWallet().getAmount();
        }
        else{
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not logged in");
            return 0;
        }
    }

    private String hashPassword(String password){

        String hashedPassword = encoder.encode(password);
        return hashedPassword;
    }

    public String resetPassword(Long userId, String oldPassword, String newPassword){
        if(checkIfUserLoggedIn(userId)){
            GomartUser user = gomartUserRepository.findById(userId).get();
            if(encoder.matches(oldPassword, user.getPassword())){
                user.setPassword(hashPassword(newPassword));
                gomartUserRepository.save(user);
                return "Password Changed Successfully";
            }
            else{
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong password");
                return "Incorrect Password";
            }
        }
        else{
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not logged in");
            return "User not logged in";
        }
    }

    public String resetPassword(Long userId){
        if(checkIfUserLoggedIn(userId)){
            GomartUser user = gomartUserRepository.findById(userId).get();
            String newPassword = RandomStringUtils.randomAlphanumeric(10);
            user.setPassword(hashPassword(newPassword));
            gomartUserRepository.save(user);
            return newPassword;
        }
        else{
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not logged in");
            return "User not logged in";
        }
    }
}