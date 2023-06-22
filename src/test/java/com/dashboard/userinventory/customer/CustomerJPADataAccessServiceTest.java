package com.dashboard.userinventory.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@Disabled
@ExtendWith(MockitoExtension.class)
class CustomerJPADataAccessServiceTest {

    private CustomerJPADataAccessService underTest;
    @Mock private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        underTest = new CustomerJPADataAccessService(customerRepository);
    }

    @Test
    void selectAllCustomers() {
        //When
        underTest.selectAllCustomers();

        //Then
        Mockito.verify(customerRepository)
                .findAll();
    }

    @Test
    void selectCustomerById() {
        //Given
        long id = 1;

        //When
        underTest.selectCustomerById(id);

        //Then
        Mockito.verify(customerRepository)
                .findById(id);
    }

    @Test
    void insertCustomer() {
        //Given
        Customer customer = new Customer(
                "Daniel Craig",
                "danielcraig@gmail.com",
                "password",
                25,
                Gender.MALE
        );

        //When
        underTest.insertCustomer(customer);

        //Then
        Mockito.verify(customerRepository)
                .save(customer);
    }

    @Test
    void existsCustomerWithEmail() {
        //Given
        String email = "danielcraig@gmail.com";

        //When
        underTest.existsCustomerWithEmail(email);

        //Then
        Mockito.verify(customerRepository)
                .existsCustomerByEmail(email);
    }

    @Test
    void existsCustomerWithId() {
        //Given
        long id = 1;

        //When
        underTest.existsCustomerWithId(id);

        //Then
        Mockito.verify(customerRepository)
                .existsCustomerById(id);
    }

    @Test
    void updateCustomer() {
        //Given
        Customer update = new Customer(
                1L,
                "Ali Fazal",
                "alifazal@gmail.com",
                25,
                Gender.MALE,
                "password"
        );

        //When
        underTest.updateCustomer(update);

        //Then
        Mockito.verify(customerRepository)
                .save(update);
    }

    @Test
    void deleteCustomerById() {
        //Given
        long id = 2;

        //Given
        underTest.deleteCustomerById(id);

        //Then
        Mockito.verify(customerRepository)
                .deleteById(id);
    }

    @Test
    void selectCustomerByEmail() {
        //Given
        String email = "danielcraig@gmail.com";

        //When
        underTest.selectCustomerByEmail(email);

        //Then
        Mockito.verify(customerRepository)
                .findCustomerByEmail(email);
    }

}