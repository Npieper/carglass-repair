package com.carglass.repair.mapper;

import com.carglass.repair.dto.RepairOrderCreateRequestDto;
import com.carglass.repair.dto.RepairOrderResponseDto;
import com.carglass.repair.dto.RepairOrderUpdateRequestDto;
import com.carglass.repair.entity.Customer;
import com.carglass.repair.entity.GlassType;
import com.carglass.repair.entity.RepairOrder;
import com.carglass.repair.entity.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class RepairOrderMapperTest {

    @Autowired
    private RepairOrderMapper repairOrderMapper;

    private static final Long CUSTOMER_ID = 5L;
    private static final String VRN = "XX123";
    private static final GlassType GLASS_TYPE = GlassType.FRONTSCHEIBE;
    private static final Status STATUS = Status.OFFEN;
    private static final LocalDate ORDER_DATE = LocalDate.of(2020, 1, 8);;

    private static final Long REPAIR_ORDER_ID = 99L;

    @Test
    void toEntityFromCreateDtoTest() {
        RepairOrderCreateRequestDto dto =
                new RepairOrderCreateRequestDto(CUSTOMER_ID, VRN, GLASS_TYPE, STATUS, ORDER_DATE);

        RepairOrder entity = repairOrderMapper.toEntity(dto);

        assertNotNull(entity);
        assertNotNull(entity.getCustomer());
        assertEquals(CUSTOMER_ID, entity.getCustomer().getId());
    }

    @Test
    void toEntityFromUpdateDtoTest() {
        RepairOrderUpdateRequestDto dto =
                new RepairOrderUpdateRequestDto(VRN, GLASS_TYPE, STATUS, ORDER_DATE);


        RepairOrder entity = repairOrderMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(VRN, entity.getVehicleRegistrationNumber());
        assertEquals(GLASS_TYPE, entity.getGlassType());
        assertEquals(ORDER_DATE, entity.getOrderDate());
        assertEquals(STATUS, entity.getStatus());
    }


    @Test
    void toDtoTest() {
        Customer customer = new Customer();
        customer.setId(CUSTOMER_ID);
        customer.setName("Max Mustermann");

        RepairOrder repairOrder = new RepairOrder();
        repairOrder.setId(REPAIR_ORDER_ID);
        repairOrder.setCustomer(customer);
        repairOrder.setStatus(STATUS);

        RepairOrderResponseDto dto = repairOrderMapper.toDto(repairOrder);

        assertNotNull(dto);
        assertEquals(REPAIR_ORDER_ID, dto.id());
        assertEquals(CUSTOMER_ID, dto.customer().id());
        assertEquals(STATUS.toString(), dto.status());
    }
}