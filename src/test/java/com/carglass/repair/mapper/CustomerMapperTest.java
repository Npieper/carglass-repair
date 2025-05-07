package com.carglass.repair.mapper;

import com.carglass.repair.dto.CustomerRequestDto;
import com.carglass.repair.dto.CustomerResponseDto;
import com.carglass.repair.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class CustomerMapperTest {

    @Autowired
    private CustomerMapper customerMapper;

    private static final String NAME = "Max Mustermann";
    private static final String EMAIL = "max@mustermann.com";
    private static final String NUMBER = "01785533023";

    @Test
    public void toEntityTest() {
        CustomerRequestDto dto = new CustomerRequestDto(NAME, EMAIL, NUMBER);
        
        Customer customer = customerMapper.toEntity(dto);
        
        assertNotNull(customer);
        assertEquals(NAME, customer.getName());
        assertEquals(EMAIL, customer.getEmail());
        assertEquals(NUMBER, customer.getPhoneNumber());
    }

    @Test
    public void toDtoTest() {
        Customer customer = new Customer();
        customer.setName(NAME);
        customer.setEmail(EMAIL);
        customer.setPhoneNumber(NUMBER);

        CustomerResponseDto dto = customerMapper.toDto(customer);
        
        assertNotNull(dto);
        assertEquals(NAME, dto.name());
        assertEquals(EMAIL, dto.email());
        assertEquals(NUMBER, dto.phoneNumber());

    }
}