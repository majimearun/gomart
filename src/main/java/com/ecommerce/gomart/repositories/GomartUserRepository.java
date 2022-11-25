package com.ecommerce.gomart.repositories;

import com.ecommerce.gomart.models.GomartUser;
import com.ecommerce.gomart.models.ManagerStatus;
import com.ecommerce.gomart.models.Role;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GomartUserRepository extends JpaRepository<GomartUser, Long> {

    List<GomartUser> findByManagerIsNotNull();

    List<GomartUser> findByRole(Role role);


}
