package com.demo.voting_system.domain.dto;

import lombok.Builder;

import java.time.OffsetDateTime;


@Builder
public record VoteResponseDto(
        Long id,
        Long voterId,
        Long electionId,
        Long optionId,
        OffsetDateTime votedAt,
        String message
) {
}
