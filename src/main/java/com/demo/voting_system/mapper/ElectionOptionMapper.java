package com.demo.voting_system.mapper;

import com.demo.voting_system.domain.dto.ElectionOptionDto;
import com.demo.voting_system.domain.entity.ElectionOption;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ElectionOptionMapper {

    ElectionOptionDto toDto(ElectionOption entity);

    @Mapping(target = "election", ignore = true)
    ElectionOption toEntity(ElectionOptionDto dto);
}
