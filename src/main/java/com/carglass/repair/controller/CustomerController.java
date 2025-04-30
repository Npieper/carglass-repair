package com.carglass.repair.controller;

import com.carglass.repair.entity.Customer;
import com.carglass.repair.exception.ResourceNotFoundException;
import com.carglass.repair.repository.CustomerRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody @Valid Customer customer) {
        Customer savedCustomer = customerRepository.save(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Kunde mit ID " + id + " nicht gefunden"));
    }
}