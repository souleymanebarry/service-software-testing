package com.barry.servicesoftwaretesting.customers.controller.impl;

import com.barry.servicesoftwaretesting.customers.controller.CustomerController;
import com.barry.servicesoftwaretesting.customers.models.Customer;
import com.barry.servicesoftwaretesting.customers.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class CustomerControllerImpl implements CustomerController {

    private final CustomerService customerService;
    @Override
    public ResponseEntity<Void> registerNewCustomer(Customer customer) {
            customerService.registerNewCustomer(customer);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
