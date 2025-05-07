package com.carglass.repair.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CustomerRequestDto(
        @NotBlank(message = "Name is mandatory")
        String name,

        @Email
        @NotBlank(message = "E-Mail is mandatory")
        String email,

        @Pattern(
                regexp = "^(\\+49|0)[1-9][0-9]{1,14}$",
                message = "Number is not valid"
        )
        @NotBlank(message = "Phone Number is mandatory")
        String phoneNumber
) {
}
