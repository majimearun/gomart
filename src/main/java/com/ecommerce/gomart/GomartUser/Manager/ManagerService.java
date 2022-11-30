package com.ecommerce.gomart.GomartUser.Manager;

import com.ecommerce.gomart.GomartUser.GomartUser;
import com.ecommerce.gomart.GomartUser.GomartUserRepository;
import com.ecommerce.gomart.Order.OrderRepository;
import com.ecommerce.gomart.Product.Product;
import com.ecommerce.gomart.Product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ManagerService {
    private final GomartUserRepository gomartUserRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Autowired
    public ManagerService(GomartUserRepository gomartUserRepository, OrderRepository orderRepository, ProductRepository productRepository){
        this.orderRepository = orderRepository;
        this.gomartUserRepository = gomartUserRepository;
        this.productRepository = productRepository;
    }

    public void addProduct(Long userId, Product product){
        if(checkManagerStatus(userId)){
            productRepository.save(product);
        }
        else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have Manager level access or is not logged in");
        }
    }

    public void updateProduct(Long userId, Product product){
        if(checkManagerStatus(userId)){
            productRepository.save(product);
        }
        else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have Manager level access or is not logged in");
        }
    }

    public void deleteProduct(Long userId, Long id){
        if(checkManagerStatus(userId)){
            productRepository.deleteById(id);
        }
        else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have Manager level access or is not logged in");
        }
    }

    public void saveImage(Long userId, Long productId, MultipartFile file) throws IOException {
        if(checkManagerStatus(userId)){
            Product product = productRepository.findById(productId).get();
            product.setImage(file.getBytes());
            productRepository.save(product);
        }
        else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have Manager level access or is not logged in");
        }
    }

    public List<Product> getAllProducts(Long userId){
        if(checkManagerStatus(userId)){
            return productRepository.findAll();
        }
        else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have Manager level access or is not logged in");
        }
    }


    private boolean checkManagerStatus(Long userId){
        Optional<GomartUser> gomartUser = gomartUserRepository.findById(userId);
        if(gomartUser.isPresent()){
            return gomartUser.get().getManager().isManagerPerms() && gomartUser.get().isLoginStatus();
        }
        return false;
    }


}
