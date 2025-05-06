package com.carglass.repair.mapper;

import com.carglass.repair.dto.RepairOrderCreateRequestDto;
import com.carglass.repair.dto.RepairOrderResponseDto;
import com.carglass.repair.dto.RepairOrderUpdateRequestDto;
import com.carglass.repair.entity.Customer;
import com.carglass.repair.entity.RepairOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;


@Mapper(componentModel = "spring", implementationName = "test")
public interface RepairOrderMapper {

    @Mapping(target = "customer", source = "customerId", qualifiedByName = "mapCustomerIdToCustomer")
    RepairOrder toEntity (RepairOrderCreateRequestDto dto);

    RepairOrder toEntity (RepairOrderUpdateRequestDto dto);

    RepairOrderResponseDto toDto(RepairOrder repairOrder);

    @Named("mapCustomerIdToCustomer")
    static Customer mapCustomerIdToCustomer(Long id) {
        if (id == null) return null;
        Customer customer = new Customer();
        customer.setId(id);
        return customer;    }
}

