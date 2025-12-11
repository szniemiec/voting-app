package com.demo.voting_system.mapper;

import com.demo.voting_system.domain.dto.VoterDto;
import com.demo.voting_system.domain.entity.Voter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface VoterMapper {
    VoterDto toDto(Voter entity);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Voter toEntity(VoterDto Dto);
}
