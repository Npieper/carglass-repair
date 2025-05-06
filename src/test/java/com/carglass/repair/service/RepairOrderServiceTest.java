package com.carglass.repair.service;

import com.carglass.repair.entity.Customer;
import com.carglass.repair.entity.GlassType;
import com.carglass.repair.entity.RepairOrder;
import com.carglass.repair.entity.Status;
import com.carglass.repair.exception.DuplicateFieldException;
import com.carglass.repair.exception.ResourceNotFoundException;
import com.carglass.repair.repository.CustomerRepository;
import com.carglass.repair.repository.RepairOrderRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RepairOrderServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private RepairOrderRepository repairOrderRepository;

    @InjectMocks
    private RepairOrderService repairOrderService;

    private static RepairOrder repairOrder;
    private static Customer customer;

    @BeforeAll
    public static void setup() {
        customer = new Customer();
        customer.setId(1L);
        customer.setName("Max Mustermann");

        repairOrder = new RepairOrder();
        repairOrder.setId(1L);
        repairOrder.setCustomer(customer);
        repairOrder.setVehicleRegistrationNumber("ABC123");
        repairOrder.setGlassType(GlassType.FRONTSCHEIBE);
        repairOrder.setStatus(Status.OFFEN);
        repairOrder.setOrderDate(LocalDate.now());
    }

    @Test
    void getAllRepairOrders_withResults_returnsList() {
        List<RepairOrder> orders = List.of(new RepairOrder(), new RepairOrder());
        when(repairOrderRepository.findAll()).thenReturn(orders);

        List<RepairOrder> result = repairOrderService.getAllRepairOrders();

        assertEquals(2, result.size());
        verify(repairOrderRepository, times(1)).findAll();
    }

    @Test
    void getAllRepairOrders_empty_returnsEmptyList() {
        when(repairOrderRepository.findAll()).thenReturn(List.of());

        List<RepairOrder> result = repairOrderService.getAllRepairOrders();

        assertTrue(result.isEmpty());
        verify(repairOrderRepository, times(1)).findAll();
    }

    @Test
    void getRepairOrderById_existingId_returnsOrder() {
        when(repairOrderRepository.findById(1L)).thenReturn(Optional.of(repairOrder));

        RepairOrder result = repairOrderService.getRepairOrderById(1L);

        assertEquals(repairOrder, result);
        verify(repairOrderRepository, times(1)).findById(1L);
    }

    @Test
    void getRepairOrderById_notFound_throwsException() {
        when(repairOrderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> repairOrderService.getRepairOrderById(1L));
    }

    @Test
    void saveRepairOrder_validOrder_savesSuccessfully() {
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(repairOrderRepository.existsByVehicleRegistrationNumber(repairOrder.getVehicleRegistrationNumber())).thenReturn(false);

        repairOrderService.saveRepairOrder(repairOrder);

        verify(repairOrderRepository, times(1)).save(repairOrder);
    }

    @Test
    void saveRepairOrder_nonExistingCustomer_throwsException() {
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> repairOrderService.saveRepairOrder(repairOrder));
    }

    @Test
    void saveRepairOrder_duplicateVRN_throwsException() {
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(repairOrderRepository.existsByVehicleRegistrationNumber(repairOrder.getVehicleRegistrationNumber())).thenReturn(true);

        assertThrows(DuplicateFieldException.class, () -> repairOrderService.saveRepairOrder(repairOrder));
    }

    @Test
    void updateRepairOrder_validUpdate_updatesSuccessfully() {
        RepairOrder updated = new RepairOrder();
        updated.setVehicleRegistrationNumber("XYZ789");
        updated.setStatus(Status.IN_BEARBEITUNG);
        updated.setGlassType(GlassType.SEITENSCHEIBE);
        updated.setOrderDate(LocalDate.now());

        when(repairOrderRepository.findById(1L)).thenReturn(Optional.of(repairOrder));
        when(repairOrderRepository.existsByVehicleRegistrationNumber("XYZ789")).thenReturn(false);
        when(repairOrderRepository.save(any())).thenReturn(repairOrder);

        RepairOrder result = repairOrderService.updateRepairOrder(1L, updated);

        assertEquals("XYZ789", result.getVehicleRegistrationNumber());
        assertEquals("IN_BEARBEITUNG", result.getStatus().toString());
        assertEquals("SEITENSCHEIBE", result.getGlassType().toString());
        verify(repairOrderRepository).save(repairOrder);
    }

    @Test
    void updateRepairOrder_duplicateVRN_throwsException() {
        RepairOrder updated = new RepairOrder();
        updated.setVehicleRegistrationNumber("DUP123");
        updated.setStatus(Status.ABGESCHLOSSEN);

        when(repairOrderRepository.existsByVehicleRegistrationNumber("DUP123")).thenReturn(true);

        assertThrows(DuplicateFieldException.class, () -> repairOrderService.updateRepairOrder(1L, updated));
    }

    @Test
    void updateRepairOrder_notExisting_throwsException() {
        RepairOrder updated = new RepairOrder();
        updated.setVehicleRegistrationNumber("XYZ123");
        updated.setStatus(Status.ABGESCHLOSSEN);

        when(repairOrderRepository.existsByVehicleRegistrationNumber("XYZ123")).thenReturn(false);
        when(repairOrderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> repairOrderService.updateRepairOrder(1L, updated));
    }

    @Test
    void deleteRepairOrder_existingOrder_deletesSuccessfully() {
        when(repairOrderRepository.findById(1L)).thenReturn(Optional.of(repairOrder));

        repairOrderService.deleteRepairOrderById(1L);

        verify(repairOrderRepository, times(1)).delete(repairOrder);
    }

    @Test
    void deleteRepairOrder_notFound_throwsException() {
        when(repairOrderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> repairOrderService.deleteRepairOrderById(1L));
    }
}