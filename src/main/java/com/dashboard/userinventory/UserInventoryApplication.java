package com.dashboard.userinventory;

import com.dashboard.userinventory.customer.Customer;
import com.dashboard.userinventory.customer.CustomerRepository;
import com.dashboard.userinventory.customer.Gender;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UserInventoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserInventoryApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(CustomerRepository customerRepository) {
		return args -> {
			//customerRepository.saveAll(CustomerListDataAccessService.getCustomerList());
			var faker = new Faker();
			int age = faker.random().nextInt(15, 100);
			Customer customer = new Customer(
					faker.name().fullName(),
					faker.internet().safeEmailAddress(),
					faker.internet().password(),
					age,
					age % 2 == 0 ? Gender.MALE : Gender.FEMALE
			);
			customerRepository.save(customer);
		};
	}

}
