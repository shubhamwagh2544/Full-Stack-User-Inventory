package com.dashboard.userinventory.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerDao customerDao;
    @Autowired
    public CustomerService(@Qualifier("jpa") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers() {
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomer(Long customerId) {
        return customerDao.selectCustomerById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                String.format("Customer with ID %s not found", customerId)
                        )
                );
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        //check if email exists
        String email = customerRegistrationRequest.email();
        if (customerDao.existsCustomerWithEmail(email)) {
            throw new DuplicateResourceException(
                    String.format("Customer with email %s already exists", email)
            );
        }

        Customer customer = new Customer(
                customerRegistrationRequest.name(),
                email,
                customerRegistrationRequest.password(),
                customerRegistrationRequest.age(),
                customerRegistrationRequest.gender()
        );

        customerDao.insertCustomer(customer);
    }

    public void updateCustomer(Long customerId, CustomerUpdateRequest customerUpdateRequest) {
        Customer customer = getCustomer(customerId);

        boolean changes = false;

        if (customer.getName() != null || !customer.getName().equals(customerUpdateRequest.name())) {
            customer.setName(customerUpdateRequest.name());
            changes = true;
        }
        if (customer.getEmail() != null || !customer.getEmail().equals(customerUpdateRequest.email())) {
            if (customerDao.existsCustomerWithEmail(customerUpdateRequest.email())) {
                throw new DuplicateResourceException(
                        String.format("Customer with email %s already exists", customerUpdateRequest.email())
                );
            }
            customer.setEmail(customerUpdateRequest.email());
            changes = true;
        }
        if (customer.getPassword() != null || !customer.getPassword().equals(customerUpdateRequest.password())) {
            customer.setPassword(customerUpdateRequest.password());
            changes = true;
        }
        if (customer.getAge() != null || !customer.getAge().equals(customerUpdateRequest.age())) {
            customer.setAge(customerUpdateRequest.age());
            changes = true;
        }
        if (customer.getGender() != null || !customer.getGender().equals(customerUpdateRequest.gender())) {
            customer.setGender(customerUpdateRequest.gender());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException(
                    "No Data Changes Found In Your Request"
            );
        }
        customerDao.updateCustomer(customer);

    }

    public void deleteCustomer(Long customerId) {
        boolean isCustomerExists = customerDao.existsCustomerWithId(customerId);
        if (isCustomerExists) {
            customerDao.deleteCustomerById(customerId);
        }
        else {
            throw new ResourceNotFoundException(
                    String.format("Customer with ID %s not found", customerId)
            );
        }
    }

}

