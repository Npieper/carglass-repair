package com.carglass.repair.dto;

public record CustomerResponseDto(
        Long id,
        String name,
        String email,
        String phoneNumber
) { }
