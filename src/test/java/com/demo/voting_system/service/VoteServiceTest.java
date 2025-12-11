package com.demo.voting_system.service;

import com.demo.voting_system.domain.dto.VoteDto;
import com.demo.voting_system.domain.dto.VoteResponseDto;
import com.demo.voting_system.domain.entity.Election;
import com.demo.voting_system.domain.entity.ElectionOption;
import com.demo.voting_system.domain.entity.Vote;
import com.demo.voting_system.domain.entity.Voter;
import com.demo.voting_system.exception.ElectionNotActiveException;
import com.demo.voting_system.exception.VoterAlreadyVotedException;
import com.demo.voting_system.repository.ElectionOptionRepository;
import com.demo.voting_system.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private VoterService voterService;

    @Mock
    private ElectionService electionService;

    @Mock
    private ElectionOptionRepository electionOptionRepository;

    @InjectMocks
    private VoteService voteService;

    private VoteDto voteDto;
    private Voter voter;
    private Election election;
    private ElectionOption option;

    @BeforeEach
    void setUp() {
        voteDto = VoteDto.builder()
                .voterId(1L)
                .electionId(1L)
                .optionId(1L)
                .build();

        voter = Voter.builder()
                .id(1L)
                .peselNumber("12345678901")
                .firstName("Jan")
                .lastName("Kowalski")
                .build();

        election = Election.builder()
                .id(1L)
                .title("Wybory na Wójta")
                .status(Election.ElectionStatus.ACTIVE)
                .startDate(OffsetDateTime.now().minusHours(1))
                .endDate(OffsetDateTime.now().plusHours(1))
                .build();

        option = ElectionOption.builder()
                .id(1L)
                .election(election)
                .label("Kandydat A")
                .build();
    }

    @Test
    void shouldCastVoteSuccessfully() {
        // Given
        when(voterService.getActiveVoterOrThrow(1L)).thenReturn(voter);
        when(electionService.getElectionById(1L)).thenReturn(election);
        when(electionService.isElectionActive(election)).thenReturn(true);
        when(electionOptionRepository.findById(1L)).thenReturn(Optional.of(option));
        when(voteRepository.save(any(Vote.class))).thenReturn(Vote.builder()
                .id(1L)
                .voter(voter)
                .election(election)
                .option(option)
                .votedAt(OffsetDateTime.now())
                .build());

        // When
        VoteResponseDto result = voteService.addVote(voteDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.voterId()).isEqualTo(1L);
        verify(voteRepository, times(1)).save(any(Vote.class));
    }

    @Test
    void shouldThrowExceptionWhenVoterAlreadyVoted() {
        // Given
        when(electionOptionRepository.findById(1L)).thenReturn(Optional.of(option));
        when(voterService.getActiveVoterOrThrow(1L)).thenReturn(voter);
        when(electionService.isElectionActive(any())).thenReturn(true);
        when(voteRepository.existsByVoterIdAndElectionId(1L, 1L)).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> voteService.addVote(voteDto))
                .isInstanceOf(VoterAlreadyVotedException.class)
                .hasMessage(String.format("Voter has already voted in election %d", voteDto.electionId()));
    }

    @Test
    void shouldThrowExceptionWhenElectionIsNotActive() {
        // Given
        election.setStatus(Election.ElectionStatus.CLOSED);
        when(electionOptionRepository.findById(1L)).thenReturn(Optional.of(option));

        // When & Then
        assertThatThrownBy(() -> voteService.addVote(voteDto))
                .isInstanceOf(ElectionNotActiveException.class)
                .hasMessage(String.format("Election with ID %d is not active", voteDto.electionId()));
    }
}