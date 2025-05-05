package com.carglass.repair.mapper;

import com.carglass.repair.dto.RepairOrderCreateRequestDto;
import com.carglass.repair.dto.RepairOrderResponseDto;
import com.carglass.repair.dto.RepairOrderUpdateRequestDto;
import com.carglass.repair.entity.RepairOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RepairOrderMapper {

    RepairOrder toEntity (RepairOrderCreateRequestDto dto);

    RepairOrderResponseDto toDto(RepairOrder repairOrder);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    void updateRepairOrderFromDto(RepairOrderUpdateRequestDto dto, @MappingTarget RepairOrder entity);
}

