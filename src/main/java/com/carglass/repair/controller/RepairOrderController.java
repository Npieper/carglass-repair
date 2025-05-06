package com.carglass.repair.controller;

import com.carglass.repair.dto.RepairOrderCreateRequestDto;
import com.carglass.repair.dto.RepairOrderResponseDto;
import com.carglass.repair.dto.RepairOrderUpdateRequestDto;
import com.carglass.repair.entity.RepairOrder;
import com.carglass.repair.entity.Status;
import com.carglass.repair.mapper.RepairOrderMapper;
import com.carglass.repair.service.RepairOrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/repairorders")
public class RepairOrderController {

    @Autowired
    private RepairOrderService repairOrderService;

    @Autowired
    private RepairOrderMapper repairOrderMapper;


    @GetMapping
    public List<RepairOrderResponseDto> getAllRepairOrders() {
        List<RepairOrder> repairOrders = repairOrderService.getAllRepairOrders();
        return repairOrders.stream()
                .map(repairOrder -> repairOrderMapper.toDto(repairOrder))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RepairOrderResponseDto> getRepairOrderById(@PathVariable @Min(1) Long id) {
        RepairOrder repairOrder = repairOrderService.getRepairOrderById(id);
        return ResponseEntity.ok(repairOrderMapper.toDto(repairOrder));
    }

    @PostMapping
    public ResponseEntity<RepairOrderResponseDto> createRepairOrder(@RequestBody @Valid
                                                                    RepairOrderCreateRequestDto repairOrderCreateRequestDto) {
        RepairOrder repairOrder = repairOrderMapper.toEntity(repairOrderCreateRequestDto);
        repairOrderService.saveRepairOrder(repairOrder);
        return ResponseEntity.created(URI.create("/repairorders/" + repairOrder.getId()))
                .body(repairOrderMapper.toDto(repairOrder));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RepairOrderResponseDto> updateCustomer(@PathVariable Long id,
                                                                 @RequestBody @Valid
                                                                 RepairOrderUpdateRequestDto repairOrderUpdateRequestDto) {
        RepairOrder updateRepairOrder = repairOrderService.updateRepairOrder(id, repairOrderMapper.toEntity(repairOrderUpdateRequestDto));
        return ResponseEntity.ok(repairOrderMapper.toDto(updateRepairOrder));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRepairOrder(@PathVariable Long id) {
        repairOrderService.deleteRepairOrderById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<RepairOrderResponseDto> updateStatus(@PathVariable Long id, @RequestBody Status status) {
        RepairOrder repairOrder = repairOrderService.updateStatus(id, status);
        return ResponseEntity.ok(repairOrderMapper.toDto(repairOrder));
    }
}