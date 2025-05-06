package com.carglass.repair.repository;

import com.carglass.repair.entity.Customer;
import com.carglass.repair.entity.RepairOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepairOrderRepository extends JpaRepository<RepairOrder, Long> {
    boolean existsByVehicleRegistrationNumber(String vehicleRegistrationNumber);

    boolean existsByCustomer(Customer customer);
}