package com.ecommerce.gomart.repositories;

import com.ecommerce.gomart.models.GomartUser;
import com.ecommerce.gomart.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomer(GomartUser user);

    List<Order> findByCustomerAndOrderDate(GomartUser user, LocalDate date);

    List<Order> findByCustomerAndOrderDateBetween(GomartUser user, LocalDate startDate, LocalDate endDate);

}
