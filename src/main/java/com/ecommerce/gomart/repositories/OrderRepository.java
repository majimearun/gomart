package com.ecommerce.gomart.repositories;

import com.ecommerce.gomart.models.GomartUser;
import com.ecommerce.gomart.models.Order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
