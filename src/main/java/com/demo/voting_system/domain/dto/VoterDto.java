package com.demo.voting_system.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;


@Builder
public record VoterDto(
        Long id,
        @NotBlank(message = "PESEL number is required")
        @Pattern(regexp = "\\d{11}", message = "PESEL must be 11 digits")
        String peselNumber,
        @NotBlank(message = "First name is required")
        String firstName,
        @NotBlank(message = "Last name is required")
        String lastName,
        Boolean blocked
) {
}
