package com.carglass.repair.dto;

import com.carglass.repair.entity.GlassType;
import com.carglass.repair.entity.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RepairOrderUpdateRequestDto(
        @NotBlank(message = "VRN is mandatory")
        String vehicleRegistrationNumber,
        @NotNull(message = "Glass Type is mandatory")
        GlassType glassType,
        @NotNull(message = "Status is mandatory")
        Status status,
        @NotNull(message = "Order Date is mandatory")
        LocalDate orderDate
) {
}
