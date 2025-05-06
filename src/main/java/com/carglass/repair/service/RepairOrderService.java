package com.carglass.repair.service;

import com.carglass.repair.entity.Customer;
import com.carglass.repair.entity.RepairOrder;
import com.carglass.repair.entity.Status;
import com.carglass.repair.exception.DuplicateFieldException;
import com.carglass.repair.exception.ResourceNotFoundException;
import com.carglass.repair.repository.CustomerRepository;
import com.carglass.repair.repository.RepairOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.carglass.repair.exception.Resource.CUSTOMER;
import static com.carglass.repair.exception.Resource.REPAIR_ORDER;

@Service
public class RepairOrderService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RepairOrderRepository repairOrderRepository;

    public List<RepairOrder> getAllRepairOrders() {
        return repairOrderRepository.findAll();
    }

    public RepairOrder getRepairOrderById(Long id) {
        return repairOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(REPAIR_ORDER, id));
    }

    public void saveRepairOrder(RepairOrder repairOrder) {
        Customer customer = customerRepository.findById(repairOrder.getCustomer().getId())
                .orElseThrow(() -> new ResourceNotFoundException(CUSTOMER, repairOrder.getCustomer().getId()));
        repairOrder.setCustomer(customer);
        validateVrnUniqueness(repairOrder.getVehicleRegistrationNumber());
        repairOrderRepository.save(repairOrder);
    }

    public RepairOrder updateRepairOrder(Long id, RepairOrder repairOrder) {
        validateVrnUniqueness(repairOrder.getVehicleRegistrationNumber());
        RepairOrder existingRepairOrder =
                repairOrderRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException(REPAIR_ORDER, id));

        existingRepairOrder.setVehicleRegistrationNumber(repairOrder.getVehicleRegistrationNumber());
        existingRepairOrder.setStatus(repairOrder.getStatus());
        existingRepairOrder.setGlassType(repairOrder.getGlassType());
        existingRepairOrder.setOrderDate(repairOrder.getOrderDate());

        return repairOrderRepository.save(existingRepairOrder);
    }

    public RepairOrder updateStatus(Long id, Status status) {
        RepairOrder repairOrder = repairOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(REPAIR_ORDER, id));

        repairOrder.setStatus(status);
        return repairOrderRepository.save(repairOrder);
    }

    public void deleteRepairOrderById(Long id) {
        RepairOrder repairOrder = repairOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(REPAIR_ORDER, id));
        repairOrderRepository.delete(repairOrder);
    }

    private void validateVrnUniqueness(String vehicleRegistrationNumber) {
        if (repairOrderRepository.existsByVehicleRegistrationNumber(vehicleRegistrationNumber)) {
            throw new DuplicateFieldException("Vehicle Registration Number");
        }
    }
}
