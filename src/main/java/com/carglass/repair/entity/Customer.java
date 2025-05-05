package com.carglass.repair.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    @Column(unique = true)
    private String email;

    @Pattern(
            regexp = "^(\\+49|0)[1-9][0-9]{1,14}$",
            message = "Ungültige Telefonnummer"
    )
    @NotBlank(message = "Phone number is mandatory")
    @Column(unique = true)
    private String phoneNumber;
}
