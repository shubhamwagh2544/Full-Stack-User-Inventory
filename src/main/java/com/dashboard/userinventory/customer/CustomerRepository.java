package com.dashboard.userinventory.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsCustomerByEmail(String email);
    boolean existsCustomerById(Long id);
    Optional<Customer> findCustomerByEmail(String email);
    @Modifying(clearAutomatically = true)
    @Query(
            "UPDATE Customer c SET c.profileImageId = ?1 WHERE c.id = ?2"
    )
    void updateProfileImageId(String profileImageId, Long customerId);
}
