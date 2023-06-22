package com.dashboard.userinventory.journey;

import com.dashboard.userinventory.customer.Customer;
import com.dashboard.userinventory.customer.CustomerRegistrationRequest;
import com.dashboard.userinventory.customer.CustomerUpdateRequest;
import com.dashboard.userinventory.customer.Gender;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@Disabled
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CustomerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;
    private static final Faker FAKER = new Faker();
    private final String CUSTOMER_URI = "/api/v1/customers";

    @Test
    void canRegisterCustomer() {

        //create registration request
        int age = FAKER.random().nextInt(15, 100);
        String name = FAKER.name().fullName();
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID() + "@foobar.com";
        String password = FAKER.internet().password();
        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name,
                email,
                password,
                age,
                gender
        );


        //send post request : addCustomer
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();


        //get all customers
        List<Customer> allCustomerList = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();


        //make sure customer is present
        Customer expected = new Customer(
                name,
                email,
                password,
                age,
                gender
        );

        assertThat(allCustomerList).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expected);

        //to get id
        var id = allCustomerList.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        expected.setId(id);


        //get customer by id
        Customer actual = webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {})
                .returnResult()
                .getResponseBody();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void canDeleteCustomer() {

        //create registration request
        int age = FAKER.random().nextInt(15, 100);
        String name = FAKER.name().fullName();
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID() + "@foobar.com";
        String password = FAKER.internet().password();
        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name,
                email,
                password,
                age,
                gender
        );

        //send post request : addCustomer
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        //get all customers
        List<Customer> allCustomerList = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {})
                .returnResult()
                .getResponseBody();

        //make sure customer is present
        Customer expected = new Customer(
                name,
                email,
                password,
                age,
                gender
        );

        assertThat(allCustomerList).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expected);

        //to get id
        var id = allCustomerList.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //delete customer
        webTestClient.delete()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        //check customer is deleted
        webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canUpdateCustomer() {

        //create registration request
        int age = FAKER.random().nextInt(15, 100);
        String name = FAKER.name().fullName();
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID() + "@foobar.com";
        String password = FAKER.internet().password();
        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;

        CustomerRegistrationRequest registrationRequest = new CustomerRegistrationRequest(
                name,
                email,
                password,
                age,
                gender
        );

        //send post request : addCustomer
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(registrationRequest), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        //get id
        List<Customer> allCustomerList = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {})
                .returnResult()
                .getResponseBody();

        var id = allCustomerList.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //changing fields
        var newName = "Lele Pons";
        var newEmail = "lelepons@gmail.com";
        var newAge = 30;
        var newGender = Gender.FEMALE;
        var newPassword = "password123";

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                newName,
                newEmail,
                newPassword,
                newAge,
                newGender
        );

        //update customer
        webTestClient.put()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), CustomerUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        //check if updated
        //get customer
        Customer actual = webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {})
                .returnResult()
                .getResponseBody();

        assertThat(actual.getName()).isEqualTo(updateRequest.name());
        assertThat(actual.getEmail()).isEqualTo(updateRequest.email());
        assertThat(actual.getAge()).isEqualTo(updateRequest.age());
        assertThat(actual.getGender()).isEqualTo(updateRequest.gender());
        assertThat(actual.getPassword()).isEqualTo(updateRequest.password());

        //OR
        Customer expected = new Customer(
                id,
                newName,
                newEmail,
                newAge,
                newGender,
                newPassword
        );
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void canGetAllCustomers() {
        List<Customer> customerList = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        assertThat(customerList).isNotEmpty();
    }

    @Test
    void canGetCustomer() {
        //create registration request
        int age = FAKER.random().nextInt(15, 100);
        String name = FAKER.name().fullName();
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID() + "@foobar.com";
        String password = FAKER.internet().password();
        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name,
                email,
                password,
                age,
                gender
        );

        //send post request : addCustomer
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();


        //get id
        Long id = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody()
                .stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //get customer by id
        Customer actual = webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        //check if same customer
        Customer expected = new Customer(
                id,
                name,
                email,
                age,
                gender,
                password
        );

        assertThat(actual).isEqualTo(expected);
    }

}
