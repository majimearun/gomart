package com.ecommerce.gomart.GomartUser.Admin;

import com.ecommerce.gomart.Email.Email;
import com.ecommerce.gomart.Email.EmailService;
import com.ecommerce.gomart.GomartUser.GomartUser;
import com.ecommerce.gomart.GomartUser.GomartUserRepository;
import com.ecommerce.gomart.GomartUser.Manager.Manager;
import com.ecommerce.gomart.GomartUser.Manager.ManagerService;
import com.ecommerce.gomart.GomartUser.Manager.ManagerStatus;
import com.ecommerce.gomart.GomartUser.Role;
import com.ecommerce.gomart.Order.Order;
import com.ecommerce.gomart.Order.OrderRepository;
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
import java.util.List;
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
            List<Order> orders = orderRepository.findByCustomerAndOrderDateBetween(user, startDate, endDate);
            List<SendOrder> send = orders.stream().map(order -> new SendOrder(order.getOrderTransactionId(), makeSnapshotProduct(order.getProduct(), order), order.getQuantity(), order.getOrderDate())).collect(Collectors.toList());
            return send;
        }
        else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have Admin level access or is not logged in");
        }
        
    }

    @Transactional
    public List<UserInfo> getCustomers(Long adminId){
        if(checkAdminStatus(adminId)){
            List<GomartUser> users = gomartUserRepository.findAll();
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
            List<SendCart> send = orders.stream().map(order -> new SendCart(makeSnapshotProduct(order.getProduct(), order), order.getQuantity())).collect(Collectors.toList());
            return send;
        }
        else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have Admin level access or is not logged in");
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
        return snapshotProduct;

    }

    private boolean checkAdminStatus(Long userId){
        Optional<GomartUser> gomartUser = gomartUserRepository.findById(userId);
        if(gomartUser.isPresent()){
            return gomartUser.get().getAdmin().isAdminPerms() && gomartUser.get().isLoginStatus();
        }
        return false;
    }



}
