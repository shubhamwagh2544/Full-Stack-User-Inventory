package com.dashboard.userinventory.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    List<Customer> selectAllCustomers();
    Optional<Customer> selectCustomerById(Long customerId);
    void insertCustomer(Customer customer);
    boolean existsCustomerWithEmail(String email);
    boolean existsCustomerWithId(Long customerId);
    void updateCustomer(Customer update);
    void deleteCustomerById(Long customerId);
    Optional<Customer> selectCustomerByEmail(String email);
    //void updateCustomerProfileImageId(String profileImageId, Long customerId);
}
