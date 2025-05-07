package com.carglass.repair.service;

import com.carglass.repair.entity.Customer;
import com.carglass.repair.exception.CustomerInUseException;
import com.carglass.repair.exception.DuplicateFieldException;
import com.carglass.repair.exception.ResourceNotFoundException;
import com.carglass.repair.repository.CustomerRepository;
import com.carglass.repair.repository.RepairOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.carglass.repair.exception.Resource.CUSTOMER;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RepairOrderRepository repairOrderRepository;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CUSTOMER, id));
    }

    public void saveCustomer(Customer customer) {
        validateCustomerUniqueness(customer.getId(), customer, false);
        customerRepository.save(customer);
    }

    public Customer updateCustomer(Long id, Customer updatedCustomer) {
        Customer existingCustomer =
                customerRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException(CUSTOMER, id));

        validateCustomerUniqueness(id, updatedCustomer, true);

        existingCustomer.setPhoneNumber(updatedCustomer.getPhoneNumber());
        existingCustomer.setName(updatedCustomer.getName());
        existingCustomer.setEmail(updatedCustomer.getEmail());
        return customerRepository.save(existingCustomer);
    }

    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CUSTOMER, id));
        validateCustomerInUse(customer);
        customerRepository.delete(customer);
    }

    private void validateCustomerUniqueness(Long id, Customer customer, boolean isUpdate) {
        if (isDuplicateEmail(id, customer, isUpdate)) {
            throw new DuplicateFieldException("email");
        }
        if (isDuplicatePhone(id, customer, isUpdate)) {
            throw new DuplicateFieldException("phoneNumber");
        }
    }

    private boolean isDuplicateEmail(Long id, Customer customer, boolean isUpdate) {
        return isUpdate
                ? customerRepository.existsByEmailAndIdNot(customer.getEmail(), id)
                : customerRepository.existsByEmail(customer.getEmail());
    }

    private boolean isDuplicatePhone(Long id, Customer customer, boolean isUpdate) {
        return isUpdate
                ? customerRepository.existsByPhoneNumberAndIdNot(customer.getPhoneNumber(), id)
                : customerRepository.existsByPhoneNumber(customer.getPhoneNumber());
    }

    private void validateCustomerInUse(Customer customer) {
        if(repairOrderRepository.existsByCustomer(customer)) {
            throw new CustomerInUseException(customer.getId());
        }

    }
}
