package com.carglass.repair.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @Email
    @NotBlank(message = "Email is mandatory")
    private String email;

    @Pattern(
            regexp = "^(\\+49|0)[1-9][0-9]{1,14}$",
            message = "Ungültige Telefonnummer"
    )
    @NotBlank(message = "Phone number is mandatory")
    private String phoneNumber;
}
