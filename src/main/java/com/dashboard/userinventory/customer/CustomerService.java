package com.dashboard.userinventory.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerDao customerDao;
    @Autowired
    public CustomerService(@Qualifier("list") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers() {
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomer(Long customerId) {
        return customerDao.selectCustomerById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(String.format("Customer with ID %s not found", customerId))
                );
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        if (customerDao.existsCustomerWithEmail(customerRegistrationRequest.email())) {
            throw new DuplicateResourceException(
                    String.format("Customer with email %s already exists", customerRegistrationRequest.email())
            );
        }

        Customer customer = new Customer(
                customerRegistrationRequest.name(),
                customerRegistrationRequest.email(),
                customerRegistrationRequest.password(),
                customerRegistrationRequest.age(),
                customerRegistrationRequest.gender()
        );

        customerDao.insertCustomer(customer);
    }

}

