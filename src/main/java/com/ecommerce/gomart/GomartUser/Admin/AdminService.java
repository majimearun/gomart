package com.ecommerce.gomart.GomartUser.Admin;

import com.ecommerce.gomart.Email.Email;
import com.ecommerce.gomart.Email.EmailService;
import com.ecommerce.gomart.GomartUser.GomartUser;
import com.ecommerce.gomart.GomartUser.GomartUserRepository;
import com.ecommerce.gomart.GomartUser.Manager.Manager;
import com.ecommerce.gomart.GomartUser.Manager.ManagerService;
import com.ecommerce.gomart.GomartUser.Manager.ManagerStatus;
import com.ecommerce.gomart.GomartUser.Role;
import com.ecommerce.gomart.Order.CustomerSnapshot;
import com.ecommerce.gomart.Order.Order;
import com.ecommerce.gomart.Order.OrderRepository;
import com.ecommerce.gomart.Order.ProductSnapshot;
import com.ecommerce.gomart.Product.Product;
import com.ecommerce.gomart.Product.ProductRepository;
import com.ecommerce.gomart.Stubs.SendCart;
import com.ecommerce.gomart.Stubs.SendOrder;
import com.ecommerce.gomart.Stubs.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

@Service
public class AdminService extends ManagerService {
    private final GomartUserRepository gomartUserRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final EmailService emailService;

    @Autowired
    AdminService(GomartUserRepository gomartUserRepository, OrderRepository orderRepository, ProductRepository productRepository, EmailService emailService){
        super(gomartUserRepository, orderRepository, productRepository);
        this.orderRepository = orderRepository;
        this.gomartUserRepository = gomartUserRepository;
        this.productRepository = productRepository;
        this.emailService = emailService;
    }
    

    public void giveManagerAccess(Long adminId, Long userId){
        if(checkAdminStatus(adminId)){
            GomartUser user = gomartUserRepository.findById(userId).get();
            user.setRole(Role.MANAGER);
            user.setManager(new Manager(true, ManagerStatus.Approved));
            gomartUserRepository.save(user);
            Email email = new Email(user.getEmail(), "Manager Access Granted", "You have been granted Manager access to Gomart. Please login to your account to continue using your Manager Account.");
            emailService.sendSimpleMail(email);
        }
        else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have Admin level access or is not logged in");
        }
    }


    public void removeManagerAccess(Long adminId, Long userId){
        if(checkAdminStatus(adminId)){
            GomartUser user = gomartUserRepository.findById(userId).get();
            user.setRole(Role.CUSTOMER);
            user.setManager(new Manager(false, null));
            gomartUserRepository.save(user);
            Email email = new Email(user.getEmail(), "Manager Access Revoked", "Your Manager access to Gomart has been revoked. You can still use the account as a Customer.");
            emailService.sendSimpleMail(email);
        }
        else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have Admin level access or is not logged in");
        }
    }

    @Transactional
    public List<SendOrder> getOrdersOfCustomerInDateRange(Long adminId, Long userId, LocalDate startDate, LocalDate endDate){
        if(checkAdminStatus(adminId)){
            GomartUser user = gomartUserRepository.findById(userId).get();
            CustomerSnapshot customerSnapshot = new CustomerSnapshot().builder()
                    .userId(user.getUserId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .phoneNumber(user.getPhoneNumber())
                    .address(user.getAddress())
                    .build(); 
            List<Order> orders = orderRepository.findByCustomerAndOrderDateBetween(customerSnapshot, startDate, endDate);
            List<SendOrder> send = orders.stream().map(order -> new SendOrder(order.getOrderTransactionId(), order.getProduct(), order.getQuantity(), order.getOrderDate())).collect(Collectors.toList());
            // reverse the list so that the most recent order is at the top
            return send.stream().sorted((o1, o2) -> o2.getOrderDate().compareTo(o1.getOrderDate())).collect(Collectors.toList());
        }
        else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have Admin level access or is not logged in");
        }
        
    }

    @Transactional
    public List<UserInfo> getCustomers(Long adminId){
        if(checkAdminStatus(adminId)){
            List<GomartUser> users = gomartUserRepository.findAll();
            users.removeIf(user -> user.getRole() == Role.ADMIN);
            List<UserInfo> send = users.stream().map(user -> new UserInfo(user.getUserId(), user.getFirstName(), user.getMiddleName(), user.getLastName(), user.getEmail(), user.getDob(), user.getAddress(), user.getPhoneNumber())).collect(Collectors.toList());
            return send;
        }
        else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have Admin level access or is not logged in");
        }
    }

    @Transactional
    public List<UserInfo> getManagers(Long adminId){
        if(checkAdminStatus(adminId)){
            List<GomartUser> users = gomartUserRepository.findByRole(Role.MANAGER);
            List<UserInfo> send = users.stream().map(user -> new UserInfo(user.getUserId(), user.getFirstName(), user.getMiddleName(), user.getLastName(), user.getEmail(), user.getDob(), user.getAddress(), user.getPhoneNumber())).collect(Collectors.toList());
            return send;
        }
        else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have Admin level access or is not logged in");
        }
    }

    @Transactional
    public List<UserInfo> getPendingManagers(Long adminId){
        if(checkAdminStatus(adminId)){
            List<GomartUser> managers = gomartUserRepository.findByManagerIsNotNull();
            managers.removeIf(manager -> manager.getManager().getManagerApplicationStatus() != ManagerStatus.Pending);
            List<UserInfo> send = managers.stream().map(user -> new UserInfo(user.getUserId(), user.getFirstName(), user.getMiddleName(), user.getLastName(), user.getEmail(), user.getDob(), user.getAddress(), user.getPhoneNumber())).collect(Collectors.toList());
            return send;
        }
        else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have Admin level access or is not logged in");
        }
    }

    @Transactional
    public List<SendCart> getItemsSoldOnADate(Long adminId, LocalDate date){
        if(checkAdminStatus(adminId)){
            List<Order> orders = orderRepository.findByOrderDate(date);
            Map<ProductSnapshot, Integer> productToQuantity = new HashMap<>();
            for(Order order : orders){
                if(productToQuantity.containsKey(order.getProduct())){
                    productToQuantity.put(order.getProduct(), productToQuantity.get(order.getProduct()) + order.getQuantity());
                }
                else{
                    productToQuantity.put(order.getProduct(), order.getQuantity());
                }
            }
            List<SendCart> send = new ArrayList<>();
            for(Map.Entry<ProductSnapshot, Integer> entry : productToQuantity.entrySet()){
                send.add(new SendCart(snapshotToProduct(entry.getKey()), entry.getValue()));
            }
            return send;

        }
        else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have Admin level access or is not logged in");
        }

    }

    @Transactional
    public void deleteUser(Long adminId, Long userId){
        if(checkAdminStatus(adminId)){
            GomartUser user = gomartUserRepository.findById(userId).get();
            if(user.getRole() == Role.ADMIN){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot delete an Admin");
            }
            else{
                Email email = new Email(user.getEmail(), "Account Deleted", "Your account has been deletedby the Admin. Please contact the Admin for more information.");
                emailService.sendSimpleMail(email);
                gomartUserRepository.delete(user);
            }
        }
        else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have Admin level access or is not logged in");
        }
    }

    private Product snapshotToProduct(ProductSnapshot productSnapshot){
        Product product = new Product();
        product.setName(productSnapshot.getName());
        product.setPrice(productSnapshot.getPrice());
        product.setOffer(productSnapshot.getOffer());
        product.setImage(productSnapshot.getImage());
        product.setDeliveryTime(productSnapshot.getDeliveryTime());
        return product;
        

    }

    private boolean checkAdminStatus(Long userId){
        Optional<GomartUser> gomartUser = gomartUserRepository.findById(userId);
        if(gomartUser.isPresent()){
            return gomartUser.get().getAdmin().isAdminPerms() && gomartUser.get().isLoginStatus();
        }
        return false;
    }



}
