package com.carglass.repair.controller;

import com.carglass.repair.dto.RepairOrderCreateRequestDto;
import com.carglass.repair.dto.RepairOrderResponseDto;
import com.carglass.repair.dto.RepairOrderUpdateRequestDto;
import com.carglass.repair.service.RepairOrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/repairorders")
public class RepairOrderController {

    @Autowired
    private RepairOrderService repairOrderService;


    @GetMapping
    public List<RepairOrderResponseDto> getAllRepairOrders() {
        return repairOrderService.getAllRepairOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RepairOrderResponseDto> getRepairOrderById(@PathVariable @Min(1) Long id) {
        return ResponseEntity.ok(repairOrderService.getRepairOrderById(id));
    }

    @PostMapping
    public ResponseEntity<RepairOrderResponseDto> createRepairOrder(@RequestBody @Valid
                                                                    RepairOrderCreateRequestDto repairOrderCreateRequestDto) {
        RepairOrderResponseDto repairOrderResponseDto = repairOrderService.saveRepairOrder(repairOrderCreateRequestDto);
        return ResponseEntity.created(URI.create("/repairorders/" + repairOrderResponseDto.id()))
                .body(repairOrderResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RepairOrderResponseDto> updateCustomer(@PathVariable Long id,
                                                                 @RequestBody @Valid
                                                                 RepairOrderUpdateRequestDto repairOrderUpdateRequestDto) {
        RepairOrderResponseDto updatedRepairOrder = repairOrderService.updateRepairOrder(id, repairOrderUpdateRequestDto);
        return ResponseEntity.ok(updatedRepairOrder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRepairOrder(@PathVariable Long id) {
        repairOrderService.deleteRepairOrderById(id);
        return ResponseEntity.noContent().build();
    }
}