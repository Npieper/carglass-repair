package com.carglass.repair.controller;

import com.carglass.repair.dto.CustomerRequestDto;
import com.carglass.repair.dto.CustomerResponseDto;
import com.carglass.repair.entity.Customer;
import com.carglass.repair.exception.DuplicateFieldException;
import com.carglass.repair.exception.ResourceNotFoundException;
import com.carglass.repair.mapper.CustomerMapper;
import com.carglass.repair.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static com.carglass.repair.exception.Resource.CUSTOMER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerService customerService;

    @MockitoBean
    private CustomerMapper customerMapper;

    ObjectMapper objectMapper = new ObjectMapper();
    private Customer customer;
    private CustomerRequestDto requestDto;
    private CustomerResponseDto responseDto;

    private final String NAME = "Max Mustermann";
    private final String EMAIL = "max@example.com";
    private final String NUMBER = "0123456789";

    @BeforeEach
    void setUp() {
        customer = createCustomer(1L, NAME, EMAIL, NUMBER);
        requestDto = new CustomerRequestDto(NAME, EMAIL, NUMBER);
        responseDto = new CustomerResponseDto(1L, NAME, EMAIL, NUMBER);
    }

    @Test
    void getAllCustomers_ShouldReturn200_WhenCustomersExist() throws Exception {
        Customer customer2 = createCustomer(2L, "Max Mustermann 2", "max2@example.com", "0123456781");
        CustomerResponseDto responseDto2 = new CustomerResponseDto(2L, "Max Mustermann 2", "max2@example.com", "0123456781");

        List<Customer> customers = List.of(customer, customer2);

        when(customerService.getAllCustomers()).thenReturn(customers);
        when(customerMapper.toDto(customer)).thenReturn(responseDto);
        when(customerMapper.toDto(customer2)).thenReturn(responseDto2);

        mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value(NAME))
                .andExpect(jsonPath("$[0].email").value(EMAIL))
                .andExpect(jsonPath("$[0].phoneNumber").value(NUMBER))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Max Mustermann 2"))
                .andExpect(jsonPath("$[1].email").value("max2@example.com"))
                .andExpect(jsonPath("$[1].phoneNumber").value("0123456781"));
    }


    @Test
    void getCustomerById_ShouldReturn200_WhenCustomerExists() throws Exception {
        when(customerService.getCustomerById(1L)).thenReturn(customer);
        when(customerMapper.toDto(customer)).thenReturn(responseDto);

        mockMvc.perform(get("/customers/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value(NAME));

    }

    @Test
    void getCustomerById_ShouldReturn404_WhenCustomerNotFound() throws Exception {
        when(customerService.getCustomerById(99L)).thenThrow(new ResourceNotFoundException(CUSTOMER, 99L));
        mockMvc.perform(get("/customers/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value(CUSTOMER.name() + " with id 99 not found"));
    }

    @Test
    void createCustomer_ShouldReturn201_WhenCustomerCreated() throws Exception {
        when(customerMapper.toEntity(requestDto)).thenReturn(customer);
        when(customerMapper.toDto(customer)).thenReturn(responseDto);

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.phoneNumber").value(NUMBER));
    }

    @Test
    void createCustomer_ShouldReturn400_WhenRequestIsInvalid() throws Exception {
        CustomerRequestDto invalid = new CustomerRequestDto("", "invalid", "");

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").exists());
    }


    @Test
    void createCustomer_ShouldReturn409_WhenEmailIsDuplicate() throws Exception {
        when(customerMapper.toEntity(requestDto)).thenReturn(customer);
        doThrow(new DuplicateFieldException("email")).when(customerService).saveCustomer(any());

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message")
                        .value("Data Integrity constraint: email has to be unique"));
    }

    @Test
    void updateCustomer_ShouldReturn200_WhenCustomerUpdated() throws Exception {
        when(customerMapper.toEntity(requestDto)).thenReturn(customer);
        when(customerService.updateCustomer(eq(1L), any())).thenReturn(customer);
        when(customerMapper.toDto(customer)).thenReturn(responseDto);

        mockMvc.perform(put("/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value(NAME));
    }


    @Test
    void updateCustomer_ShouldReturn404_WhenCustomerNotFound() throws Exception {
        when(customerMapper.toEntity(requestDto)).thenReturn(customer);
        when(customerService.updateCustomer(eq(5L), any())).thenThrow(new ResourceNotFoundException(CUSTOMER, 5L));

        mockMvc.perform(put("/customers/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(CUSTOMER.name() + " with id 5 not found"));

    }

    @Test
    void deleteCustomer_ShouldReturn204_WhenCustomerDeleted() throws Exception {
        mockMvc.perform(delete("/customers/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteCustomer_ShouldReturn404_WhenCustomerNotFound() throws Exception {
        doThrow(new ResourceNotFoundException(CUSTOMER, 1L)).when(customerService).deleteCustomer(1L);

        mockMvc.perform(delete("/customers/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(CUSTOMER.name() + " with id 1 not found"));
    }

    private Customer createCustomer(Long id, String name, String email, String phoneNumber) {
        Customer customer = new Customer();
        customer.setId(id);
        customer.setName(name);
        customer.setEmail(email);
        customer.setPhoneNumber(phoneNumber);
        return customer;
    }

}