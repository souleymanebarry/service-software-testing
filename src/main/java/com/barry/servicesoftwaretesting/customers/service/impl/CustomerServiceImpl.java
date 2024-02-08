package com.barry.servicesoftwaretesting.customers.service.impl;

import com.barry.servicesoftwaretesting.customers.models.Customer;
import com.barry.servicesoftwaretesting.customers.repositories.CustomerRepository;
import com.barry.servicesoftwaretesting.customers.service.CustomerService;
import com.barry.servicesoftwaretesting.utils.PhoneNumberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final PhoneNumberValidator phoneNumberValidator;

    @Override
    public void registerNewCustomer(Customer customer) {
        String phoneNumber = customer.getPhoneNumber();
        if (!phoneNumberValidator.validate(phoneNumber)) {
            throw new IllegalArgumentException(
                    String.format("Phone number %s is not Valid", phoneNumber));
        }
        Optional<Customer> costumerOpt = customerRepository.findByPhoneNumber(phoneNumber);
        if (costumerOpt.isPresent()) {
            Customer customer1 = costumerOpt.get();
            if (customer1.getName().equals(customer.getName())) {
                return;
            }
            throw new IllegalArgumentException(String.format("phone number [%s] is taken", phoneNumber));
        }
        customerRepository.save(customer);
    }
}
