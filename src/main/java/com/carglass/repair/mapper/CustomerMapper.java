package com.carglass.repair.mapper;

import com.carglass.repair.dto.CustomerRequestDto;
import com.carglass.repair.dto.CustomerResponseDto;
import com.carglass.repair.entity.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    Customer toEntity (CustomerRequestDto dto);

    CustomerResponseDto toDto(Customer customer);

}
