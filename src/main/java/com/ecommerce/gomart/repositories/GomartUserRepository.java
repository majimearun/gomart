package com.ecommerce.gomart.repositories;

import com.ecommerce.gomart.models.Customer;
import com.ecommerce.gomart.models.GomartUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GomartUserRepository extends JpaRepository<GomartUser, Long> {


}
