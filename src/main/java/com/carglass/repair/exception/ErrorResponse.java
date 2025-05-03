package com.carglass.repair.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
public class ErrorResponse {

    private int status;

    //private Map<String, String> errors;

    private String message;

    private LocalDateTime timestamp;

}