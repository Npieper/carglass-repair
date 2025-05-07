package com.carglass.repair.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Schema(description = "Error response class")
public class ErrorResponse {

    @Schema(description = "HTTP-Statuscode", example = "400")
    private int status;

    @Schema(description = "Short error message", example = "Json not valid")
    private String message;

    @Schema(description = "Time of error", example = "2025-05-06T16:01:25.3809611")
    private LocalDateTime timestamp;

}