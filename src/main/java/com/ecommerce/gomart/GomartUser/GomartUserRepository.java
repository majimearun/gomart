package com.ecommerce.gomart.GomartUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GomartUserRepository extends JpaRepository<GomartUser, Long> {

    List<GomartUser> findByManagerIsNotNull();

    List<GomartUser> findByRole(Role role);

    Optional<GomartUser> findByEmail(String email);


}
