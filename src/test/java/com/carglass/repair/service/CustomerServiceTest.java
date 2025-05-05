package com.carglass.repair.service;

import com.carglass.repair.entity.Customer;
import com.carglass.repair.exception.DuplicateFieldException;
import com.carglass.repair.exception.ResourceNotFoundException;
import com.carglass.repair.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private static Customer customer;

    @BeforeAll
    public static void setup(){
        customer = new Customer();
        customer.setEmail("email@mustermann.de");
        customer.setPhoneNumber("01778377666");
        customer.setId(1L);
        customer.setName("Max Mustermann");
    }


    @Test
    void getAllCustomers_withCustomer_returnsListOfCustomers() {
        List<Customer> customers = List.of(new Customer(), new Customer());
        when(customerRepository.findAll()).thenReturn(customers);

        List<Customer> result = customerService.getAllCustomers();

        assertEquals(2, result.size());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void getAllCustomers_withoutCustomer_returnsEmptyList() {
        List<Customer> customers = List.of();
        when(customerRepository.findAll()).thenReturn(customers);

        List<Customer> result = customerService.getAllCustomers();

        assertEquals(0, result.size());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void getCustomerById_withValidId_returnsCorrectCustomer() {
        when(customerRepository.findById(1L)).thenReturn(ofNullable(customer));

        Customer result = customerService.getCustomerById(1L);

        assertEquals(customer, result);
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void getCustomerById_notExistingId_throwsException() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerService.getCustomerById(1L));
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void saveCustomer_withValidCustomer_savesSuccessfully() {

        when(customerRepository.existsByEmail(customer.getEmail())).thenReturn(false);
        when(customerRepository.existsByPhoneNumber(customer.getPhoneNumber())).thenReturn(false);

        customerService.saveCustomer(customer);

        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void saveCustomer_withDuplicateEmail_throwsException() {
        when(customerRepository.existsByEmail(customer.getEmail())).thenReturn(true);
        assertThrows(DuplicateFieldException.class, () -> customerService.saveCustomer(customer));
    }

    @Test
    void saveCustomer_withDuplicatePhoneNumber_throwsException() {
        when(customerRepository.existsByPhoneNumber(customer.getPhoneNumber())).thenReturn(true);
        assertThrows(DuplicateFieldException.class, () -> customerService.saveCustomer(customer));
    }

    @Test
    void updateCustomer_validUpdateData_updatesSuccessfully() {
        String updatedName = "New Name";
        String updatedEmail = "new@example.com";
        String updatedPhoneNumber = "0175111111111";

        Customer updated = new Customer();
        updated.setEmail(updatedEmail);
        updated.setPhoneNumber(updatedPhoneNumber);
        updated.setName(updatedName);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.existsByEmail(updatedEmail)).thenReturn(false);
        when(customerRepository.existsByPhoneNumber(updatedPhoneNumber)).thenReturn(false);
        when(customerRepository.save(any())).thenReturn(customer);

        Customer result = customerService.updateCustomer(1L, updated);

        assertEquals(updatedName, result.getName());
        assertEquals(updatedPhoneNumber, result.getPhoneNumber());
        assertEquals(updatedEmail, result.getEmail());

        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void updateCustomer_withDuplicatePhoneNumber_throwsException() {
        String updatedEmail = "new@example.com";
        String updatedPhoneNumber = "0175111111111";

        Customer updatedCustomer = new Customer();
        updatedCustomer.setEmail(updatedEmail);
        updatedCustomer.setPhoneNumber(updatedPhoneNumber);
        updatedCustomer.setName("New Name");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.existsByEmail(updatedEmail)).thenReturn(false);
        when(customerRepository.existsByPhoneNumber(updatedPhoneNumber)).thenReturn(true);

        assertThrows(DuplicateFieldException.class, () -> customerService.updateCustomer(1L, updatedCustomer));

    }

    @Test
    void deleteCustomer_existingCustomer_deletesSuccessfully() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        customerService.deleteCustomer(1L);

        verify(customerRepository, times(1)).delete(customer);
    }

    @Test
    void deleteCustomer_notExisting_throwsResourceNotFoundException() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerService.deleteCustomer(1L));
    }
}
