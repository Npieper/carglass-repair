package com.carglass.repair.controller;

import com.carglass.repair.dto.CustomerRequestDto;
import com.carglass.repair.dto.CustomerResponseDto;
import com.carglass.repair.entity.Customer;
import com.carglass.repair.mapper.CustomerMapper;
import com.carglass.repair.service.CustomerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customers")
@Validated
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerMapper customerMapper;

    @GetMapping
    public List<CustomerResponseDto> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        return customers.stream()
                .map(customer -> customerMapper.toDto(customer))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> getCustomerById(@PathVariable @Min(1) Long id) {
        Customer customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customerMapper.toDto(customer));
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDto> createCustomer(@RequestBody @Valid
                                                                  CustomerRequestDto customerRequestDto) {
        Customer customer = customerMapper.toEntity(customerRequestDto);
        customerService.saveCustomer(customer);
        return ResponseEntity.created(URI.create("/customers/" + customer.getId()))
                .body(customerMapper.toDto(customer));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> updateCustomer(@PathVariable Long id,
                                                              @Valid @RequestBody
                                                              CustomerRequestDto customerRequestDto) {
        Customer updateCustomer = customerService.updateCustomer(id, customerMapper.toEntity(customerRequestDto));
        return ResponseEntity.ok(customerMapper.toDto(updateCustomer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}