package com.carglass.repair.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CustomerRequestDto(
        @NotBlank(message = "Name ist erforderlich")
        String name,

        @Email
        @NotBlank(message = "E-Mail ist erforderlich")
        String email,

        @Pattern(
                regexp = "^(\\+49|0)[1-9][0-9]{1,14}$",
                message = "Ungültige Telefonnummer"
        )
        @NotBlank(message = "Telefonnummer ist erforderlich")
        String phoneNumber
) {
}
