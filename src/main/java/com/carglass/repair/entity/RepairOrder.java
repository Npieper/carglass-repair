package com.carglass.repair.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Data
public class RepairOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(unique = true)
    private String vehicleRegistrationNumber;

    @Enumerated(EnumType.STRING)
    private GlassType glassType;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDate orderDate;

}
