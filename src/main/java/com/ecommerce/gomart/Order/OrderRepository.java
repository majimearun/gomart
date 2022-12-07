package com.ecommerce.gomart.Order;

import com.ecommerce.gomart.GomartUser.GomartUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomer(CustomerSnapshot customer);

    List<Order> findByCustomerAndOrderDate(CustomerSnapshot customer, LocalDate date);

    List<Order> findByCustomerAndOrderDateBetween(CustomerSnapshot customer, LocalDate startDate, LocalDate endDate);

    List<Order> findByOrderDate(LocalDate date);

}
