package com.demo.voting_system.mapper;

import com.demo.voting_system.domain.dto.ElectionDto;
import com.demo.voting_system.domain.entity.Election;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;


@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {ElectionOptionMapper.class}
)
public interface ElectionMapper {
    ElectionDto toDto(Election entity);

    Election toEntity(ElectionDto dto);
}
