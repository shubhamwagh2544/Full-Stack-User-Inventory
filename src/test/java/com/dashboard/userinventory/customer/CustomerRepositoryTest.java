package com.dashboard.userinventory.customer;

import com.dashboard.userinventory.AbstractTestContainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestContainers {

    @Autowired
    private CustomerRepository underTest;
    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
        System.out.println(applicationContext.getBeanDefinitionCount());
    }

    @Test
    void existsCustomerByEmail() {
        //Given
        int age = FAKER.random().nextInt(15, 100);
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                FAKER.internet().password(),
                age,
                age % 2 == 0 ? Gender.MALE : Gender.FEMALE
        );
        underTest.save(customer);

        //When
        boolean actual = underTest.existsCustomerByEmail(email);

        //Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerById() {
        //Given
        int age = FAKER.random().nextInt(15, 100);
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                FAKER.internet().password(),
                age,
                age % 2 == 0 ? Gender.MALE : Gender.FEMALE
        );
        underTest.save(customer);

        //When
        Long id = underTest.findAll()
                .stream()
                .filter(customer1 -> customer1.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        boolean actual = underTest.existsCustomerById(id);

        //Then
        assertThat(actual).isTrue();
    }

    @Test
    void findCustomerByEmail() {
        //Given
        int age = FAKER.random().nextInt(15, 100);
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                FAKER.internet().password(),
                age,
                age % 2 == 0 ? Gender.MALE : Gender.FEMALE
        );
        underTest.save(customer);

        //When
        Optional<Customer> customerByEmail = underTest.findCustomerByEmail(email);

        //Then
        assertThat(customerByEmail).isPresent();
        assertThat(customerByEmail).isNotEmpty();
    }

    //TODO : Write remaining Unit Tests
}