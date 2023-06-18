package com.dashboard.userinventory.journey;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.boot.test.context.SpringBootTest.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CustomerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;
    private static final Faker FAKER = new Faker();
    @Test
    void canRegisterCustomer() {

    }
}
