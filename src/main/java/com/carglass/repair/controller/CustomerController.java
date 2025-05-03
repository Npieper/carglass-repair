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

import static com.carglass.repair.exception.Resource.CUSTOMER;

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
                .orElseThrow(() -> new ResourceNotFoundException(CUSTOMER, id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer newCustomer) {

        System.out.println("In Update Customer");
        Customer customerToUpdate =
                customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CUSTOMER, id));

        customerToUpdate.setPhoneNumber(newCustomer.getPhoneNumber());
        customerToUpdate.setName(newCustomer.getName());
        customerToUpdate.setEmail(newCustomer.getEmail());
        customerRepository.save(customerToUpdate);

        return ResponseEntity.ok(customerToUpdate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CUSTOMER, id));
        customerRepository.delete(customer);
        return ResponseEntity.noContent().build();
    }
}