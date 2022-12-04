package com.ecommerce.gomart.GomartUser.Customer;

import com.ecommerce.gomart.Cart.Cart;
import com.ecommerce.gomart.Cart.CartRepository;
import com.ecommerce.gomart.Email.Email;
import com.ecommerce.gomart.Email.EmailService;
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

import me.xdrop.fuzzywuzzy.FuzzySearch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

@Service
public class CustomerService {

    private final GomartUserRepository gomartUserRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final EmailService emailService;

    @Autowired
    CustomerService(GomartUserRepository gomartUserRepository, OrderRepository orderRepository, CartRepository cartRepository, ProductRepository productRepository, EmailService emailService){
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.gomartUserRepository = gomartUserRepository;
        this.productRepository = productRepository;
        this.emailService = emailService;
    }

    public ResponseEntity<String> signUp(String password, String firstName, String lastName, LocalDate dob, String email, double amount, String address, String phone){
        try{
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
            Email email1 = new Email().builder()
                    .to(email)
                    .subject("Welcome to GoMart")
                    .body("Welcome to GoMart. We are glad to have you on board. We hope you have a great experience with us. Happy Shopping!")
                    .build();
            emailService.sendSimpleMail(email1);
            return new ResponseEntity<String>("Customer created successfully", HttpStatus.CREATED);
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cold not signup: " + e.getMessage());
        }
    }

    public LoginDetails login(String email, String password){
        Optional<GomartUser> gomartUser = gomartUserRepository.findByEmail(email);
        if(gomartUser.isPresent()){
            if(encoder.matches(password, gomartUser.get().getPassword())){
                gomartUser.get().setLoginStatus(true);
                gomartUserRepository.save(gomartUser.get());
                return new LoginDetails(gomartUser.get().getUserId(), gomartUser.get().getRole());
            }
            else{
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Password");
            }
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist");
        }
    }

    public ResponseEntity<String> logout(Long userId){
        Optional<GomartUser> gomartUser = gomartUserRepository.findById(userId);
        if(gomartUser.isPresent()){
            gomartUser.get().setLoginStatus(false);
            gomartUserRepository.save(gomartUser.get());
            return new ResponseEntity<String>("Logged out successfully", HttpStatus.OK);
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not logged in with current session");
        }
    }

    @Transactional
    public Product getProductById(Long id) {
        Optional<Product> p1 = productRepository.findById(id);
        return p1.get();
    }

    @Transactional
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @Transactional
    public List<Product> getProductsByCategory(int category) {
        return productRepository.findByCategory(Category.values()[category]);
    }

    @Transactional
    public List<Product> getProductsByName(String name) {
        List<Product> products = productRepository.findByNameIgnoreCaseContaining(name);
        if(products.isEmpty()){
            return getProductsByFuzzyName(name);
        }
        return products;
    }

    @Transactional
    public List<Product> getProductsByFuzzyName(String name) {
        List<Product> products = getProducts();
        List<Product> filtered = products.stream()
                .filter(product -> FuzzySearch.weightedRatio(product.getName(), name) > 50)
                .collect(Collectors.toList());
        List<Product> fProducts = filtered.stream()
                .sorted((p1, p2) -> FuzzySearch.weightedRatio(p2.getName(), name) - FuzzySearch.weightedRatio(p1.getName(), name))
                .collect(Collectors.toList());
        return fProducts.subList(0, Math.min(5, fProducts.size()));
    }

    @Transactional
    public List<Product> getProductsInCategoryByPriceRange(int category, double min, double max) {
        return productRepository.findByCategoryAndPriceBetween(Category.values()[category], min, max);
    }

    @Transactional
    public GomartUser getUserInfo(Long userId){
        if(checkIfUserLoggedIn(userId)){
            Optional<GomartUser> user = gomartUserRepository.findById(userId);
            return user.get();
        }
        else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not logged in");
        }
        
    }

    @Transactional
    public List<SendCart> getCart(Long userId){
        if(checkIfUserLoggedIn(userId)){
            GomartUser user = gomartUserRepository.findById(userId).get();
            List<Cart> carts =  cartRepository.findByCustomer(user);
            List<SendCart> send = carts.stream().map(cart -> new SendCart(cart.getProduct(), cart.getQuantity())).collect(Collectors.toList());
            return send;
    
        }
        else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not logged in");
        }
        
    }

    @Transactional
    public List<SendOrder> getOrders(Long userId){
        if(checkIfUserLoggedIn(userId)){
            GomartUser user = gomartUserRepository.findById(userId).get();
            List<Order> orders = orderRepository.findByCustomer(user);
            List<SendOrder> send = orders.stream().map(order -> new SendOrder(order.getOrderTransactionId(),makeSnapshotProduct(order.getProduct(), order), order.getQuantity(), order.getOrderDate())).collect(Collectors.toList());
            return send;
        }
        else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not logged in");
        }
    }

    private Product makeSnapshotProduct(Product product, Order order){
        Product snapshotProduct = new Product();
        snapshotProduct.setProductId(product.getProductId());
        snapshotProduct.setName(order.getProductNameSnapshot());
        snapshotProduct.setCategory(product.getCategory());
        snapshotProduct.setPrice(order.getProductPriceSnapshot());
        snapshotProduct.setQuantity(product.getQuantity());
        snapshotProduct.setOffer(order.getProductOfferSnapshot());
        snapshotProduct.setDeliveryTime(product.getDeliveryTime());
        snapshotProduct.setImage(product.getImage());
        return snapshotProduct;

    }

    @Transactional
    public List<SendOrder> getOrdersByOrderDate(Long userId, LocalDate date){
        if(checkIfUserLoggedIn(userId)){
            GomartUser user = gomartUserRepository.findById(userId).get();
            List<Order> orders = orderRepository.findByCustomerAndOrderDate(user, date);
            List<SendOrder> send = orders.stream().map(order -> new SendOrder(order.getOrderTransactionId(),order.getProduct(), order.getQuantity(), order.getOrderDate())).collect(Collectors.toList());
            return send;
        }
        else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not logged in");
        }
    }
    
    @Transactional
    public List<SendOrder> getOrdersByOrderDateRange(Long userId, LocalDate startDate, LocalDate endDate){
        if(checkIfUserLoggedIn(userId)){
            GomartUser user = gomartUserRepository.findById(userId).get();
            List<Order> orders = orderRepository.findByCustomerAndOrderDateBetween(user, startDate, endDate);
            List<SendOrder> send = orders.stream().map(order -> new SendOrder(order.getOrderTransactionId(),order.getProduct(), order.getQuantity(), order.getOrderDate())).collect(Collectors.toList());
            return send;
        
        }
        else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not logged in");
        }
    }

    public ResponseEntity<String> updateUserInfo(Long userId, String firstName, String middleName, String lastName, LocalDate dob, String email, String address, String phone){
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
            return new ResponseEntity<String>("User info updated successfully", HttpStatus.OK);
        }
        else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not logged in");
        }

    }

    public ResponseEntity<String> deleteUserInfo(Long userId){
        if(checkIfUserLoggedIn(userId)){
            gomartUserRepository.deleteById(userId);
            return new ResponseEntity<String>("User info deleted successfully", HttpStatus.OK);
        }
        else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not logged in");
        }
    }

    public ResponseEntity<String> addToCart(Long userId, Long productId, Integer quantity){
        if(quantity <= 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be greater than 0");
        }
        if(checkIfUserLoggedIn(userId)){
            GomartUser user = gomartUserRepository.findById(userId).get();
            Product product = productRepository.findById(productId).get();
            // check if product is already in cart
            Optional<Cart> cart = cartRepository.findByCustomerAndProduct(user, product);
            if(cart.isPresent()){
                Cart newCart = cart.get();
                if(newCart.getQuantity() + quantity > product.getQuantity()){
                    newCart.setQuantity(product.getQuantity());
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
                    cartRepository.save(newCart);
                }
                else{
                    newCart.setQuantity(quantity);
                    cartRepository.save(newCart);
                }

            }
            return new ResponseEntity<String>("Product added to cart successfully", HttpStatus.OK);
            
        }
        else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not logged in");
        }
    }

    public ResponseEntity<String> removeFromCart(Long userId, Long productId){
        if(checkIfUserLoggedIn(userId)){
            GomartUser user = gomartUserRepository.findById(userId).get();
            Product product = productRepository.findById(productId).get();
            cartRepository.deleteById(cartRepository.findByCustomerAndProduct(user, product).get().getEntryId());
            return new ResponseEntity<String>("Product removed from cart successfully", HttpStatus.OK);
        }
        else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not logged in");
        }
        
    }

    @Transactional
    public ResponseEntity<String> checkOutFromCart(Long userId){
        if(checkIfUserLoggedIn(userId)){
            GomartUser user = gomartUserRepository.findById(userId).get();
            List<Cart> cartList = cartRepository.findByCustomer(user);
            double total = 0;
            int sentQuantity = 0;
            for(Cart cart : cartList){
                sentQuantity += cart.getQuantity();
            }
            if(sentQuantity==0){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart is empty");
            }
            for(Cart cart: cartList){
                if(cart.getProduct().getQuantity() < cart.getQuantity()){
                    if(cart.getProduct().getQuantity() == 0){
                        cartRepository.deleteById(cart.getEntryId());
                    }
                    else{
                        cart.setQuantity(cart.getProduct().getQuantity());
                        cartRepository.save(cart);
                    }
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product " + cart.getProduct().getName() + " is out of stock");
                }
                else{
                    total += (100 - cart.getProduct().getOffer())/100 * cart.getProduct().getPrice() * cart.getQuantity();
                }

                }
            if(total > user.getCustomer().getWallet().getAmount()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient balance");
            }
            else{
                // make a note of all product names and quantities he has bought
                String products = "";
                for(Cart cart: cartList){
                    products += cart.getProduct().getName() + ": " + cart.getQuantity() + " Pieces,\n";
                }
                for(Cart cart: cartList){
                    Order order = new Order().builder()
                            .customer(cart.getCustomer())
                            .product(cart.getProduct())
                            .quantity(cart.getQuantity())
                            .orderDate(LocalDate.now())
                            .productNameSnapshot(cart.getProduct().getName())
                            .productPriceSnapshot(cart.getProduct().getPrice())
                            .productOfferSnapshot(cart.getProduct().getOffer())
                            .build();
                    orderRepository.save(order);
                    cartRepository.deleteById(cart.getEntryId());
                    // decrease quantity of product
                    Product product = cart.getProduct();
                    product.setQuantity(product.getQuantity() - cart.getQuantity());
                    productRepository.save(product);
                }
                user.getCustomer().getWallet().setAmount(user.getCustomer().getWallet().getAmount() - total);
                Long adminId = gomartUserRepository.findByRole(Role.ADMIN).get(0).getUserId();
                GomartUser admin = gomartUserRepository.findById(adminId).get();
                admin.getCustomer().getWallet().setAmount(admin.getCustomer().getWallet().getAmount() + total);
                gomartUserRepository.save(admin);
                gomartUserRepository.save(user);
                // send email to user
                Email email = new Email();
                email.setTo(user.getEmail());
                email.setSubject("Order placed successfully!!");
                email.setBody("Your order has been placed successfully. You have bought the following products:\n" + products + "Total amount: " + total + "\non " + LocalDate.now() + ".\n" + "Thank you for shopping with us.");
                emailService.sendSimpleMail(email);
                return new ResponseEntity<String>("Order placed successfully", HttpStatus.OK);
            }
        }
        else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not logged in");
        }
        
        
    }

    
    public ResponseEntity<String> topUpWallet(Long userId, double amount){
        if(checkIfUserLoggedIn(userId)){
            if(amount > 0){
                GomartUser user = gomartUserRepository.findById(userId).get();
                user.getCustomer().getWallet().setAmount(user.getCustomer().getWallet().getAmount() + amount);
                gomartUserRepository.save(user);
                return new ResponseEntity<String>("Wallet topped up successfully", HttpStatus.OK);
            }
            else{
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount cannot be negative");
            }
        }
        else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not logged in");
        }
    }

    public ResponseEntity<String> changeQuantityOfProductInCart(Long userId, Long productId, Integer quantity){

        if(checkIfUserLoggedIn(userId)){
            GomartUser user = gomartUserRepository.findById(userId).get();
            Product product = productRepository.findById(productId).get();
            Cart cart = cartRepository.findByCustomerAndProduct(user, product).get();
            cart.setQuantity(quantity);
            cartRepository.save(cart);
            return new ResponseEntity<String>("Quantity changed successfully", HttpStatus.OK);
        }
        else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not logged in");
        }
    }

    private boolean checkIfUserLoggedIn(Long userId){
        GomartUser user = gomartUserRepository.findById(userId).get();
        return user.isLoginStatus();
    }

    public ResponseEntity<String> applyAsManager(Long userId){
        if(checkIfUserLoggedIn(userId)){
            GomartUser user = gomartUserRepository.findById(userId).get();
            user.getManager().setManagerApplicationStatus(ManagerStatus.Pending);
            gomartUserRepository.save(user);
            return new ResponseEntity<String>("Application submitted successfully", HttpStatus.OK);
        }
        else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not logged in");
        }
    }

    @Transactional
    public double getWalletBalance(Long userId){
        if(checkIfUserLoggedIn(userId)){
            GomartUser user = gomartUserRepository.findById(userId).get();
            return user.getCustomer().getWallet().getAmount();
        }
        else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not logged in");
        }
    }

    private String hashPassword(String password){

        String hashedPassword = encoder.encode(password);
        return hashedPassword;
    }

    public ResponseEntity<String> resetPassword(Long userId, String oldPassword, String newPassword){
        if(checkIfUserLoggedIn(userId)){
            GomartUser user = gomartUserRepository.findById(userId).get();
            if(encoder.matches(oldPassword, user.getPassword())){
                user.setPassword(hashPassword(newPassword));
                gomartUserRepository.save(user);
                return new ResponseEntity<String>("Password changed successfully", HttpStatus.OK);
            }
            else{
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Wrong password entered");
            }
        }
        else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not logged in");
        }
    }
}