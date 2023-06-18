package com.dashboard.userinventory.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)         // no need to create AutoCloseable for MockitoAnnotations
class CustomerServiceTest {

    private CustomerService underTest;
    @Mock
    private CustomerDao customerDao;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }

    @Test
    void canGetAllCustomers() {
        //When
        underTest.getAllCustomers();

        //Then
        verify(customerDao)                     //static import for Mockito
                .selectAllCustomers();
    }

    @Test
    void canGetCustomer() {
        //Given
        long id = 1;
        Customer customer = new Customer(
                id,
                "Ali Fazal",
                "alifazal@gmail.com",
                25,
                Gender.MALE,
                "pass"
        );

        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        //When
        Customer actual = underTest.getCustomer(id);

        //Then
        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void willThrowIfGetCustomerReturnsEmptyOptional() {
        //Given
        long id = 1;
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());

        //When
        //Then
        assertThatThrownBy(() -> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(String.format("Customer with ID %s not found", id));
    }

    @Test
    void canAddCustomer() {     //flow similar to addCustomer
        //Given
        String email = "alifazal@gmail.com";
        when(customerDao.existsCustomerWithEmail(email)).thenReturn(false);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "Ali Fazal",
                email,
                "pass",
                25,
                Gender.MALE
        );

        //when
        underTest.addCustomer(request);

        //then
        ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).insertCustomer(argumentCaptor.capture());       //capture customer while inserting

        Customer actual = argumentCaptor.getValue();

        assertThat(actual.getId()).isNull();
        assertThat(actual.getName()).isEqualTo(request.name());
        assertThat(actual.getEmail()).isEqualTo(request.email());
        assertThat(actual.getAge()).isEqualTo(request.age());
        assertThat(actual.getPassword()).isEqualTo(request.password());
        assertThat(actual.getGender()).isEqualTo(request.gender());
    }

    @Test
    void canThrowExceptionInAddCustomerIfEmailExists() {
        //given
        String email = "alifazal@gmail.com";
        when(customerDao.existsCustomerWithEmail(email)).thenReturn(true);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "Ali Fazal",
                email,
                "pass",
                25,
                Gender.MALE
        );

        //when
        //then
        assertThatThrownBy(() -> underTest.addCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining(
                        String.format("Customer with email %s already exists", email)
                );

        verify(customerDao, never()).insertCustomer(any());
    }

    @Test
    void canDeleteCustomer() {
        //given
        long id = 1;
        when(customerDao.existsCustomerWithId(id)).thenReturn(true);

        //when
        underTest.deleteCustomer(id);

        //then
        verify(customerDao).deleteCustomerById(id);
    }

    @Test
    void canThrowExceptionInDeleteCustomerIfCustomerNotExist() {
        //given
        long id = 1;
        when(customerDao.existsCustomerWithId(id)).thenReturn(false);

        //when
        //then
        assertThatThrownBy(() -> underTest.deleteCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        String.format("Customer with ID %s not found", id)
                );
    }

    @Test
    void canUpdateCustomerName() {      //flow as per updateCustomer
        //given
        long id = 1;
        Customer customer = new Customer(
                id,
                "Ali Fazal",
                "alifazal@gmail.com",
                25,
                Gender.MALE,
                "pass"
        );

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newName = "NewName ALi Fazal";
        CustomerUpdateRequest request = new CustomerUpdateRequest(
                newName,            //new name
                "alifazal@gmail.com",
                "pass",
                25,
                Gender.MALE
        );

        //when
        underTest.updateCustomer(id, request);

        //then
        ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(argumentCaptor.capture());

        Customer actual = argumentCaptor.getValue();

        assertThat(actual.getId()).isEqualTo(1);
        assertThat(actual.getName()).isEqualTo(newName);
        assertThat(actual.getAge()).isEqualTo(request.age());
        assertThat(actual.getEmail()).isEqualTo(request.email());
        assertThat(actual.getGender()).isEqualTo(request.gender());
        assertThat(actual.getPassword()).isEqualTo(request.password());
    }

    @Test
    void canUpdateCustomerEmail() {      //flow as per updateCustomer
        //given
        long id = 1;
        Customer customer = new Customer(
                id,
                "Ali Fazal",
                "alifazal@gmail.com",
                25,
                Gender.MALE,
                "pass"
        );

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "NewEmailForalifazal@gmail.com";
        CustomerUpdateRequest request = new CustomerUpdateRequest(
                "Ali Fazal",
                newEmail,        //new email
                "pass",
                25,
                Gender.MALE
        );

        //when
        underTest.updateCustomer(id, request);

        //then
        ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(argumentCaptor.capture());

        Customer actual = argumentCaptor.getValue();

        assertThat(actual.getId()).isEqualTo(1);
        assertThat(actual.getName()).isEqualTo(request.name());
        assertThat(actual.getAge()).isEqualTo(request.age());
        assertThat(actual.getEmail()).isEqualTo(newEmail);
        assertThat(actual.getGender()).isEqualTo(request.gender());
        assertThat(actual.getPassword()).isEqualTo(request.password());
    }

    @Test
    void canThrowExceptionInUpdateCustomerIfEmailAlreadyExists() {      //flow as per updateCustomer
        //given
        long id = 1;
        Customer customer = new Customer(
                id,
                "Ali Fazal",
                "alifazal@gmail.com",
                25,
                Gender.MALE,
                "pass"
        );

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String email = "alreadyexistingemail@gmail.com";
        CustomerUpdateRequest request = new CustomerUpdateRequest(
                "Ali Fazal",
                email,
                "pass",
                25,
                Gender.MALE
        );

        when(customerDao.existsCustomerWithEmail(email)).thenReturn(true);

        //when
        //then
        assertThatThrownBy(() -> underTest.updateCustomer(id, request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining(
                        String.format("Customer with email %s already exists", email)
                );

        verify(customerDao, never()).updateCustomer(any());
    }

    @Test
    void canUpdateCustomerAge() {      //flow as per updateCustomer
        //given
        long id = 1;
        Customer customer = new Customer(
                id,
                "Ali Fazal",
                "alifazal@gmail.com",
                25,
                Gender.MALE,
                "pass"
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        int newAge = 30;
        CustomerUpdateRequest request = new CustomerUpdateRequest(
                "Ali Fazal",
                "alifazal@gmail.com",
                "pass",
                newAge,
                Gender.MALE
        );

        //when
        underTest.updateCustomer(id, request);

        //then
        ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(argumentCaptor.capture());

        Customer actual = argumentCaptor.getValue();

        assertThat(actual.getId()).isEqualTo(1);
        assertThat(actual.getName()).isEqualTo(request.name());
        assertThat(actual.getAge()).isEqualTo(newAge);
        assertThat(actual.getEmail()).isEqualTo(request.email());
        assertThat(actual.getGender()).isEqualTo(request.gender());
        assertThat(actual.getPassword()).isEqualTo(request.password());
    }

    @Test
    void canUpdateCustomerGender() {      //flow as per updateCustomer
        //given
        long id = 1;
        Customer customer = new Customer(
                id,
                "Ali Fazal",
                "alifazal@gmail.com",
                25,
                Gender.MALE,
                "pass"
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        Gender newGender = Gender.FEMALE;
        CustomerUpdateRequest request = new CustomerUpdateRequest(
                "Ali Fazal",
                "alifazal@gmail.com",
                "pass",
                25,
                newGender
        );

        //when
        underTest.updateCustomer(id, request);

        //then
        ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(argumentCaptor.capture());

        Customer actual = argumentCaptor.getValue();

        assertThat(actual.getId()).isEqualTo(1);
        assertThat(actual.getName()).isEqualTo(request.name());
        assertThat(actual.getAge()).isEqualTo(request.age());
        assertThat(actual.getEmail()).isEqualTo(request.email());
        assertThat(actual.getGender()).isEqualTo(newGender);
        assertThat(actual.getPassword()).isEqualTo(request.password());
    }

    @Test
    void canUpdateCustomerPassword() {      //flow as per updateCustomer
        //given
        long id = 1;
        Customer customer = new Customer(
                id,
                "Ali Fazal",
                "alifazal@gmail.com",
                25,
                Gender.MALE,
                "pass"
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newPassword = "password123";
        CustomerUpdateRequest request = new CustomerUpdateRequest(
                "Ali Fazal",
                "alifazal@gmail.com",
                newPassword,
                25,
                Gender.MALE
        );

        //when
        underTest.updateCustomer(id, request);

        //then
        ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(argumentCaptor.capture());

        Customer actual = argumentCaptor.getValue();

        assertThat(actual.getId()).isEqualTo(1);
        assertThat(actual.getName()).isEqualTo(request.name());
        assertThat(actual.getAge()).isEqualTo(request.age());
        assertThat(actual.getEmail()).isEqualTo(request.email());
        assertThat(actual.getGender()).isEqualTo(request.gender());
        assertThat(actual.getPassword()).isEqualTo(newPassword);
    }

    @Test
    void canThrowExceptionWhenNoChangesInUpdateCustomer() {
        //given
        long id = 1;
        Customer customer = new Customer(
                id,
                "Ali Fazal",
                "alifazal@gmail.com",
                25,
                Gender.MALE,
                "pass"
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest request = new CustomerUpdateRequest(
                customer.getName(),
                customer.getEmail(),
                customer.getPassword(),
                customer.getAge(),
                customer.getGender()
        );

        //when
        assertThatThrownBy(() -> underTest.updateCustomer(id, request))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("No Data Changes Found In Your Request");

        //then
        verify(customerDao, never()).updateCustomer(any());

    }

}