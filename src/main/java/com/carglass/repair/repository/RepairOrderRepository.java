package com.carglass.repair.repository;

import com.carglass.repair.entity.Customer;
import com.carglass.repair.entity.RepairOrder;
import com.carglass.repair.entity.Status;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepairOrderRepository extends CrudRepository<RepairOrder, Long> {
    //List<RepairOrder> findByCustomer(Customer customer);
    //List<RepairOrder> findByStatus(Status status);
}