package com.carglass.repair.service;

import com.carglass.repair.dto.RepairOrderCreateRequestDto;
import com.carglass.repair.dto.RepairOrderResponseDto;
import com.carglass.repair.dto.RepairOrderUpdateRequestDto;
import com.carglass.repair.entity.Customer;
import com.carglass.repair.entity.RepairOrder;
import com.carglass.repair.exception.DuplicateFieldException;
import com.carglass.repair.exception.ResourceNotFoundException;
import com.carglass.repair.mapper.RepairOrderMapper;
import com.carglass.repair.repository.CustomerRepository;
import com.carglass.repair.repository.RepairOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.carglass.repair.exception.Resource.CUSTOMER;
import static com.carglass.repair.exception.Resource.REPAIR_ORDER;

@Service
public class RepairOrderService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RepairOrderRepository repairOrderRepository;

    @Autowired
    private RepairOrderMapper repairOrderMapper;

    public List<RepairOrderResponseDto> getAllRepairOrders() {
        List<RepairOrder> repairOrders = repairOrderRepository.findAll();
        return repairOrders.stream()
                .map(repairOrder -> repairOrderMapper.toDto(repairOrder))
                .collect(Collectors.toList());
    }

    public RepairOrderResponseDto getRepairOrderById(Long id) {
        RepairOrder repairOrder = repairOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(REPAIR_ORDER, id));
        return repairOrderMapper.toDto(repairOrder);
    }

    public RepairOrderResponseDto saveRepairOrder(RepairOrderCreateRequestDto repairOrderCreateRequestDto) {
        Customer customer = customerRepository.findById(repairOrderCreateRequestDto.customerId())
                .orElseThrow(() -> new ResourceNotFoundException(CUSTOMER, repairOrderCreateRequestDto.customerId()));
        RepairOrder repairOrder = repairOrderMapper.toEntity(repairOrderCreateRequestDto);
        repairOrder.setCustomer(customer);
        validateVrnUniqueness(repairOrder.getVehicleRegistrationNumber());
        repairOrderRepository.save(repairOrder);
        return repairOrderMapper.toDto(repairOrder);
    }

    public RepairOrderResponseDto updateRepairOrder(Long id, RepairOrderUpdateRequestDto repairOrderUpdateRequestDto) {
        validateVrnUniqueness(repairOrderUpdateRequestDto.vehicleRegistrationNumber());
        RepairOrder existingRepairOrder =
                repairOrderRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException(REPAIR_ORDER, id));

        repairOrderMapper.updateRepairOrderFromDto(repairOrderUpdateRequestDto, existingRepairOrder);
        return repairOrderMapper.toDto(repairOrderRepository.save(existingRepairOrder));
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
