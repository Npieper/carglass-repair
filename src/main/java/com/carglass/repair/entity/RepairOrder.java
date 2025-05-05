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

    @NotBlank(message = "VRN is mandatory")
    @Column(unique = true)
    private String vehicleRegistrationNumber;

    @NotNull(message = "Glass Type is mandatory")
    @Enumerated(EnumType.STRING)
    private GlassType glassType;

    @NotNull(message = "Status is mandatory")
    @Enumerated(EnumType.STRING)
    private Status status;

    @NotNull(message = "Order Date is mandatory")
    private LocalDate orderDate;

}
