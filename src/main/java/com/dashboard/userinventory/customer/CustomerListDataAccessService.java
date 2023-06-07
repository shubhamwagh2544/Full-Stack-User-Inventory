package com.dashboard.userinventory.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDao {

    //fake db
    private static List<Customer> CUSTOMER_LIST = new ArrayList<>();
    static {
        CUSTOMER_LIST.add(
                new Customer(
                        1L,
                        "janet jones",
                        "janetjones@gmail.com",
                        25,
                        Gender.FEMALE,
                        "Password",
                        "null"
                )
        );
        CUSTOMER_LIST.add(
                new Customer(
                        2L,
                        "antonio junior",
                        "antoniojunior@gmail.com",
                        20,
                        Gender.MALE,
                        "Password",
                        "null"
                )
        );
        CUSTOMER_LIST.add(
                new Customer(
                        3L,
                        "Chris Battles",
                        "chrisbattles@gmail.com",
                        22,
                        Gender.MALE,
                        "Password",
                        "null"
                )
        );
        CUSTOMER_LIST.add(
                new Customer(
                        4L,
                        "henry richards",
                        "henryrichards@gmail.com",
                        28,
                        Gender.MALE,
                        "Password",
                        "null"
                )
        );
        CUSTOMER_LIST.add(
                new Customer(
                        5L,
                        "maria metrics",
                        "mariametrics@gmail.com",
                        24,
                        Gender.FEMALE,
                        "Password",
                        "null"
                )
        );
    }

    @Override
    public List<Customer> selectAllCustomers() {
        return CUSTOMER_LIST;
    }

    @Override
    public Optional<Customer> selectCustomerById(Long customerId) {
        Optional<Customer> customer1 = CUSTOMER_LIST
                .stream()
                .filter(customer -> customer.getId().equals(customerId))
                .findFirst();
        if (customer1 == null) {
            throw new ResourceNotFoundException(String.format("Customer with ID %s not found", customerId));
        }
        return customer1;
    }

    @Override
    public void insertCustomer(Customer customer) {
        CUSTOMER_LIST.add(customer);
    }

    @Override
    public boolean existsCustomerWithEmail(String email) {
        return CUSTOMER_LIST
                .stream()
                .anyMatch(customer -> customer.getEmail().equals(email));
    }

    @Override
    public boolean existsCustomerWithId(Long customerId) {
        return CUSTOMER_LIST
                .stream()
                .anyMatch(customer -> customer.getId().equals(customerId));
    }

    @Override
    public void updateCustomer(Customer update) {
        CUSTOMER_LIST.add(update);
    }

    @Override
    public void deleteCustomerById(Long customerId) {
        CUSTOMER_LIST
                .stream()
                .filter(customer -> customer.getId().equals(customerId))
                .findFirst()
                .ifPresent(CUSTOMER_LIST::remove);
    }

    @Override
    public Optional<Customer> selectCustomerByEmail(String email) {
        return CUSTOMER_LIST
                .stream()
                .filter(customer -> customer.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public void updateCustomerProfileImageId(String profileImageId, Long customerId) {
        //TODO : to do later :)
    }
}
