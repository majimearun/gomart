package com.ecommerce.gomart.repositories;

import com.ecommerce.gomart.models.GomartUser;
import com.ecommerce.gomart.models.Order;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomer(GomartUser user);

    List<Order> findByCustomerAndOrderDate(GomartUser user, LocalDate date);

    List<Order> findByCustomerAndOrderDateBetween(GomartUser user, LocalDate startDate, LocalDate endDate);

}
