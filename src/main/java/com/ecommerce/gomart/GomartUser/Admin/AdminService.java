package com.ecommerce.gomart.GomartUser.Admin;

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

@Service
public class AdminService extends ManagerService {
    private final GomartUserRepository gomartUserRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Autowired
    AdminService(GomartUserRepository gomartUserRepository, OrderRepository orderRepository, ProductRepository productRepository){
        super(gomartUserRepository, orderRepository, productRepository);
        this.orderRepository = orderRepository;
        this.gomartUserRepository = gomartUserRepository;
        this.productRepository = productRepository;
    }

    public void giveManagerAccess(Long adminId, Long userId){
        if(checkAdminStatus(adminId)){
            GomartUser user = gomartUserRepository.findById(userId).get();
            user.setRole(Role.MANAGER);
            user.setManager(new Manager(true, ManagerStatus.Approved));
            gomartUserRepository.save(user);
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
        }
        else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have Admin level access or is not logged in");
        }
    }

    public List<SendOrder> getOrdersOfCustomerInDateRange(Long adminId, Long userId, LocalDate startDate, LocalDate endDate){
        if(checkAdminStatus(adminId)){
            GomartUser user = gomartUserRepository.findById(userId).get();
            List<Order> orders = orderRepository.findByCustomerAndOrderDateBetween(user, startDate, endDate);
            List<SendOrder> send = orders.stream().map(order -> new SendOrder(order.getOrderTransactionId(), order.getProduct(), order.getQuantity(), order.getOrderDate())).collect(Collectors.toList());
            return send;
        }
        else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have Admin level access or is not logged in");
        }
        
    }

    public List<UserInfo> getCustomers(Long adminId){
        if(checkAdminStatus(adminId)){
            List<GomartUser> users = gomartUserRepository.findByRole(Role.CUSTOMER);
            List<UserInfo> send = users.stream().map(user -> new UserInfo(user.getUserId(), user.getFirstName(), user.getMiddleName(), user.getLastName(), user.getEmail(), user.getDob(), user.getAddress(), user.getPhoneNumber())).collect(Collectors.toList());
            return send;
        }
        else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have Admin level access or is not logged in");
        }
    }


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

    private boolean checkAdminStatus(Long userId){
        Optional<GomartUser> gomartUser = gomartUserRepository.findById(userId);
        if(gomartUser.isPresent()){
            return gomartUser.get().getAdmin().isAdminPerms() && gomartUser.get().isLoginStatus();
        }
        return false;
    }



}
