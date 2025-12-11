package com.demo.voting_system.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ElectionOptionDto(
        Long id,
        @NotBlank(message = "Label is required")
        String label,
        String description) {
}
