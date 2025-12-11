package com.demo.voting_system.domain.dto;

import com.demo.voting_system.domain.entity.Election;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.Set;

@Builder
public record ElectionDto(
        Long id,
        @NotBlank(message = "Title is required")
        @Size(min = 3, max = 255, message = "Title must be between 3 and 255 characters")
        String title,
        String description,
        Election.ElectionStatus status,
        @NotNull(message = "Start date is required")
        OffsetDateTime startDate,
        @NotNull(message = "End date is required")
        OffsetDateTime endDate,
        @NotEmpty(message = "At least one option is required")
        @Valid
        Set<ElectionOptionDto> options
) {
}
