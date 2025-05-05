package com.carglass.repair.dto;

import java.time.LocalDate;

public record RepairOrderResponseDto(
        Long id,
        CustomerResponseDto customer,
        String vehicleRegistrationNumber,
        String glassType,
        String status,
        LocalDate orderDate
) {
}
