package com.demo.voting_system.service;

import com.demo.voting_system.domain.dto.VoterDto;
import com.demo.voting_system.domain.entity.Voter;
import com.demo.voting_system.exception.ResourceNotFoundException;
import com.demo.voting_system.exception.VoterAlreadyBlockedException;
import com.demo.voting_system.exception.VotingSystemException;
import com.demo.voting_system.mapper.VoterMapper;
import com.demo.voting_system.repository.VoterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoterServiceTest {

    @Mock
    private VoterRepository voterRepository;

    @Mock
    private VoterMapper voterMapper;

    @InjectMocks
    private VoterService voterService;

    private VoterDto voterDto;
    private Voter voter;

    @BeforeEach
    void setUp() {
        voterDto = VoterDto.builder()
                .peselNumber("12345678901")
                .firstName("Jan")
                .lastName("Kowalski")
                .build();

        voter = Voter.builder()
                .id(1L)
                .peselNumber("10987654321")
                .firstName("John")
                .lastName("Doe")
                .build();
    }

    @Test
    void shouldCreateVoterSuccessfully() {
        // Given
        when(voterRepository.existsByPeselNumber(voterDto.peselNumber())).thenReturn(false);
        when(voterRepository.save(any(Voter.class))).thenReturn(voter);
        when(voterMapper.toEntity(voterDto)).thenReturn(voter);
        when(voterMapper.toDto(voter)).thenReturn(voterDto);

        // When
        VoterDto result = voterService.addVoter(voterDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.peselNumber()).isEqualTo(voterDto.peselNumber());
        verify(voterRepository, times(1)).save(any(Voter.class));
    }

    @Test
    void shouldThrowExceptionWhenVoterAlreadyExists() {
        // Given
        when(voterRepository.existsByPeselNumber(voterDto.peselNumber())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> voterService.addVoter(voterDto))
                .isInstanceOf(VotingSystemException.class)
                .hasMessage(String.format("Voter with PESEL %s already exists", voterDto.peselNumber()));
    }

    @Test
    void shouldBlockVoterSuccessfully() {
        // Given
        when(voterRepository.findById(1L)).thenReturn(Optional.of(voter));
        voter.setBlocked(false);
        when(voterRepository.save(any(Voter.class))).thenReturn(voter);
        when(voterMapper.toDto(voter)).thenReturn(voterDto);

        // When
        VoterDto result = voterService.blockVoter(1L);

        // Then
        assertThat(result).isNotNull();
        verify(voterRepository, times(1)).save(any(Voter.class));
    }

    @Test
    void shouldThrowExceptionWhenBlockingAlreadyBlockedVoter() {
        // Given
        voter.setBlocked(true);
        when(voterRepository.findById(1L)).thenReturn(Optional.of(voter));

        // When & Then
        assertThatThrownBy(() -> voterService.blockVoter(1L))
                .isInstanceOf(VoterAlreadyBlockedException.class);
    }

    @Test
    void shouldThrowExceptionWhenVoterNotFound() {
        // Given
        when(voterRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> voterService.getVoterById(999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}