package com.carglass.repair.controller;

import com.carglass.repair.entity.RepairOrder;
import com.carglass.repair.service.RepairOrderService;
import jakarta.validation.Valid;
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
    public List<RepairOrder> getAllRepairOrders() {
        return repairOrderService.getAllRepairOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RepairOrder> getRepairOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(repairOrderService.getRepairOrderById(id));
    }

    @PostMapping
    public ResponseEntity<RepairOrder> createRepairOrder(@RequestBody @Valid RepairOrder repairOrder) {
        repairOrderService.saveRepairOrder(repairOrder);
        return ResponseEntity.created(URI.create("/repairorders/" + repairOrder.getId()))
                .body(repairOrder);
    }


    @PutMapping("/{id}")
    public ResponseEntity<RepairOrder> updateCustomer(@PathVariable Long id,
                                                      @RequestBody RepairOrder repairOrder) {
        RepairOrder updatedRepairOrder = repairOrderService.updateRepairOrder(id, repairOrder);
        return ResponseEntity.ok(updatedRepairOrder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRepairOrder(@PathVariable Long id) {
        repairOrderService.deleteRepairOrderById(id);
        return ResponseEntity.noContent().build();
    }
}