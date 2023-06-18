package com.dashboard.userinventory.customer;

import com.dashboard.userinventory.AbstractTestContainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerJDBCDataAccessServiceTest extends AbstractTestContainers {

    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }

    @Test
    void selectAllCustomers() {
        //Given
        int age = FAKER.random().nextInt(15, 100);
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                FAKER.internet().password(),
                age,
                age % 2 == 0 ? Gender.MALE : Gender.FEMALE
        );
        underTest.insertCustomer(customer);

        //When
        List<Customer> actual = underTest.selectAllCustomers();

        //Then
        assertThat(actual).isNotEmpty();
    }

    @Test
    void selectCustomerById() {
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
        underTest.insertCustomer(customer);

        //When
        Long actual = underTest.selectAllCustomers()
                .stream()
                .filter(customer1 -> customer1.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        Optional<Customer> optional = underTest.selectCustomerById(actual);

        //Then
        assertThat(optional).isPresent().hasValueSatisfying(customer1 -> {
            assertThat(customer1.getId()).isEqualTo(actual);
            assertThat(customer1.getName()).isEqualTo(customer.getName());
            assertThat(customer1.getEmail()).isEqualTo(customer.getEmail());
            assertThat(customer1.getPassword()).isEqualTo(customer.getPassword());
            assertThat(customer1.getAge()).isEqualTo(customer.getAge());
            assertThat(customer1.getGender()).isEqualTo(customer.getGender());
        });
    }

    @Test
    void willReturnEmptyWhenSelectCustomerById() {
        //Given
        long id = -1;

        //When
        Optional<Customer> customer = underTest.selectCustomerById(id);

        //Then
        assertThat(customer).isEmpty();
    }

    @Test
    void insertCustomer() {
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

        //When
        underTest.insertCustomer(customer);

        Long id = underTest.selectAllCustomers()
                .stream()
                .filter(customer1 -> customer1.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        boolean actual = underTest.existsCustomerWithId(id);

        //Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerWithEmail() {
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
        underTest.insertCustomer(customer);

        //When
        boolean actual = underTest.existsCustomerWithEmail(email);

        //Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerWithEmailReturnsFalseWhenDoesNotExist() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        //When
        boolean actual = underTest.existsCustomerWithEmail(email);

        //Then
        assertThat(actual).isFalse();
    }

    @Test
    void existsCustomerWithId() {
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
        underTest.insertCustomer(customer);

        //When
        Long id = underTest.selectAllCustomers()
                .stream()
                .filter(customer1 -> customer1.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        boolean actual = underTest.existsCustomerWithId(id);

        //Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerWithIdReturnsFalseWhenIdNotPresent() {
        //Given
        long id = -1;

        //When
        boolean actual = underTest.existsCustomerWithId(id);

        //Then
        assertThat(actual).isFalse();

    }

    @Test
    void deleteCustomerById() {
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
        underTest.insertCustomer(customer);

        //When
        Long id = underTest.selectAllCustomers()
                .stream()
                .filter(customer1 -> customer1.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        underTest.deleteCustomerById(id);

        boolean actual = underTest.existsCustomerWithId(id);

        //Then
        assertThat(actual).isFalse();

        //OR
        Optional<Customer> customer1 = underTest.selectCustomerById(id);
        assertThat(customer1).isNotPresent();
    }

    @Test
    void selectCustomerByEmail() {
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
        underTest.insertCustomer(customer);

        //When
        Optional<Customer> customer1 = underTest.selectCustomerByEmail(email);

        //Then
        assertThat(customer1).isPresent();

    }

    @Test
    void selectCustomerByEmailReturnsFalseWhenEmailNotExists() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        //When
        Optional<Customer> customer = underTest.selectCustomerByEmail(email);

        //Then
        assertThat(customer).isNotPresent();
    }

    @Test
    void updateCustomerName() {
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
        underTest.insertCustomer(customer);

        //When
        Long id = underTest.selectAllCustomers()
                .stream()
                .filter(customer1 -> customer1.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        Customer update = new Customer();
        var newName = "Daniel Craig";
        update.setId(id);
        update.setName(newName);
        underTest.updateCustomer(update);

        Optional<Customer> actual = underTest.selectCustomerById(id);
        //Then
        assertThat(actual).isPresent().hasValueSatisfying(customer1 -> {
            assertThat(customer1.getId()).isEqualTo(id);
            assertThat(customer1.getName()).isEqualTo(newName);
            assertThat(customer1.getEmail()).isEqualTo(email);
            assertThat(customer1.getAge()).isEqualTo(customer.getAge());
            assertThat(customer1.getPassword()).isEqualTo(customer.getPassword());
            assertThat(customer1.getGender()).isEqualTo(customer.getGender());
        });
    }

    @Test
    void updateCustomerEmail() {
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
        underTest.insertCustomer(customer);

        //When
        Long id = underTest.selectAllCustomers()
                .stream()
                .filter(customer1 -> customer1.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        Customer update = new Customer();
        var newEmail = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        update.setId(id);
        update.setEmail(newEmail);
        underTest.updateCustomer(update);

        Optional<Customer> actual = underTest.selectCustomerById(id);

        //Then
        assertThat(actual).isPresent().hasValueSatisfying(customer1 -> {
            assertThat(customer1.getId()).isEqualTo(id);
            assertThat(customer1.getName()).isEqualTo(customer.getName());
            assertThat(customer1.getEmail()).isEqualTo(newEmail);
            assertThat(customer1.getAge()).isEqualTo(customer.getAge());
            assertThat(customer1.getPassword()).isEqualTo(customer.getPassword());
            assertThat(customer1.getGender()).isEqualTo(customer.getGender());
        });
    }

    @Test
    void updateCustomerAge() {
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
        underTest.insertCustomer(customer);

        //When
        Long id = underTest.selectAllCustomers()
                .stream()
                .filter(customer1 -> customer1.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        Customer update = new Customer();
        int newAge = 25;                //new age
        update.setId(id);
        update.setAge(newAge);
        underTest.updateCustomer(update);

        Optional<Customer> actual = underTest.selectCustomerById(id);

        //Then
        assertThat(actual).isPresent().hasValueSatisfying(customer1 -> {
            assertThat(customer1.getId()).isEqualTo(id);
            assertThat(customer1.getName()).isEqualTo(customer.getName());
            assertThat(customer1.getEmail()).isEqualTo(customer.getEmail());
            assertThat(customer1.getAge()).isEqualTo(newAge);
            assertThat(customer1.getPassword()).isEqualTo(customer.getPassword());
            assertThat(customer1.getGender()).isEqualTo(customer.getGender());
        });
    }

    @Test
    void updateCustomerPassword() {
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
        underTest.insertCustomer(customer);

        //When
        Long id = underTest.selectAllCustomers()
                .stream()
                .filter(customer1 -> customer1.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        Customer update = new Customer();
        String password = "TestPassword";             //new Pass
        update.setId(id);
        update.setPassword(password);
        underTest.updateCustomer(update);

        Optional<Customer> actual = underTest.selectCustomerById(id);

        //Then
        assertThat(actual).isPresent().hasValueSatisfying(customer1 -> {
            assertThat(customer1.getId()).isEqualTo(id);
            assertThat(customer1.getName()).isEqualTo(customer.getName());
            assertThat(customer1.getEmail()).isEqualTo(customer.getEmail());
            assertThat(customer1.getAge()).isEqualTo(customer.getAge());
            assertThat(customer1.getPassword()).isEqualTo(password);
            assertThat(customer1.getGender()).isEqualTo(customer.getGender());
        });
    }

    @Test
    void updateCustomerGender() {
        //Given
        int age = FAKER.random().nextInt(15, 100);
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                FAKER.internet().password(),
                age,
                Gender.FEMALE
        );
        underTest.insertCustomer(customer);

        //When
        Long id = underTest.selectAllCustomers()
                .stream()
                .filter(customer1 -> customer1.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        Customer update = new Customer();
        update.setId(id);
        Gender male = Gender.MALE;
        update.setGender(male);
        underTest.updateCustomer(update);

        Optional<Customer> actual = underTest.selectCustomerById(id);

        //Then
        assertThat(actual).isPresent().hasValueSatisfying(customer1 -> {
            assertThat(customer1.getId()).isEqualTo(id);
            assertThat(customer1.getName()).isEqualTo(customer.getName());
            assertThat(customer1.getEmail()).isEqualTo(customer.getEmail());
            assertThat(customer1.getAge()).isEqualTo(customer.getAge());
            assertThat(customer1.getPassword()).isEqualTo(customer.getPassword());
            assertThat(customer1.getGender()).isEqualTo(male);
        });
    }

}