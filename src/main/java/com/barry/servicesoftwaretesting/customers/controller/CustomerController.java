package com.barry.servicesoftwaretesting.customers.controller;


import com.barry.servicesoftwaretesting.customers.models.Customer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1")
public interface CustomerController {

    @PostMapping("/customers")
    ResponseEntity<Void> registerNewCustomer(@RequestBody Customer customer);
}
