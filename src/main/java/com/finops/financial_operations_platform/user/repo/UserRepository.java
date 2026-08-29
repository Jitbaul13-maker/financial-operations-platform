package com.finops.financial_operations_platform.user.repo;

import com.finops.financial_operations_platform.user.model.User;
import org.hibernate.internal.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<User> findByCustomerId(String customerId);

    boolean existsByCustomerId(String customerId);
}
