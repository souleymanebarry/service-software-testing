package com.barry.servicesoftwaretesting;

import com.barry.servicesoftwaretesting.customers.models.Customer;
import com.barry.servicesoftwaretesting.customers.repositories.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Optional;

@SpringBootApplication
@Slf4j
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner start(CustomerRepository repository) {
		return args -> {
			repository.save(Customer.builder().id(null).name("john").phoneNumber("+447095641000").build());
			repository.save(Customer.builder().id(null).name("Aisha").phoneNumber("+447095618000").build());
			repository.save(Customer.builder().id(null).name("Céline").phoneNumber("+447095901000").build());

			final Optional<Customer> customer = repository.findByPhoneNumber("+447095618000");
			customer.ifPresent(x -> {
				log.info("UUID: {}", customer.get().getId());
				log.info("-------------------------");
				log.info("Name: {}", customer.get().getName());
				log.info("-------------------------");
				log.info("Phone number: {}", customer.get().getPhoneNumber());
			});
		};
	}

}
