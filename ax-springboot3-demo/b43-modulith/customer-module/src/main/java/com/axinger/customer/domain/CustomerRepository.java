package com.axinger.customer.domain;

import com.axinger.customer.CustomerModule;
import com.axinger.customer.CustomerModule.Customer;
import com.axinger.shared.CustomerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 客户仓储接口
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, CustomerId> {

    Optional<Customer> findByEmail(CustomerModule.Email email);

    Optional<Customer> findByPhone(CustomerModule.PhoneNumber phone);

    boolean existsByEmail(CustomerModule.Email email);

    boolean existsByPhone(CustomerModule.PhoneNumber phone);

    long countByStatus(CustomerModule.CustomerStatus status);
}