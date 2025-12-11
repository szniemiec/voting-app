package com.demo.voting_system.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record VoteDto(
        @NotNull(message = "Voter ID is required")
        Long voterId,
        @NotNull(message = "Election ID is required")
        Long electionId,
        @NotNull(message = "Option ID is required")
        Long optionId
) {
}
