package com.demo.voting_system.service;

import com.demo.voting_system.domain.dto.VoterDto;
import com.demo.voting_system.domain.entity.Voter;
import com.demo.voting_system.exception.ResourceNotFoundException;
import com.demo.voting_system.exception.VoterAlreadyBlockedException;
import com.demo.voting_system.exception.VoterBlockedException;
import com.demo.voting_system.exception.VotingSystemException;
import com.demo.voting_system.mapper.VoterMapper;
import com.demo.voting_system.repository.VoterRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class VoterService {
    private final VoterRepository voterRepository;
    private final VoterMapper voterMapper;

    @Transactional
    public VoterDto addVoter(VoterDto voterDto) {
        log.info("Creating new voter with PESEL: {}", voterDto.peselNumber());

        if (voterRepository.existsByPeselNumber(voterDto.peselNumber())) {
            log.warn("Voter with PESEL {} already exists", voterDto.peselNumber());
            throw new VotingSystemException(
                    String.format("Voter with PESEL %s already exists", voterDto.peselNumber())
            );
        }

        Voter voter = voterMapper.toEntity(voterDto);
        Voter savedVoter = voterRepository.save(voter);
        log.info("Voter created successfully with ID: {}", savedVoter.getId());
        return voterMapper.toDto(savedVoter);
    }

    @Transactional
    public VoterDto blockVoter(Long id) {
        log.info("Blocking voter with ID: {}", id);
        Voter voter = voterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Voter with ID %d not found", id)
                ));

        if (voter.getBlocked()) {
            log.warn("Voter with ID {} is already blocked", id);
            throw new VoterAlreadyBlockedException(
                    String.format("Voter with ID %d is already blocked", id)
            );
        }

        voter.setBlocked(true);
        Voter updatedVoter = voterRepository.save(voter);
        log.info("Voter with ID {} has been blocked", id);
        return voterMapper.toDto(updatedVoter);
    }

    @Transactional
    public VoterDto unblockVoter(Long id) {
        log.info("Unblocking voter with ID: {}", id);
        Voter voter = voterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Voter with ID %d not found", id)
                ));

        voter.setBlocked(false);
        Voter updatedVoter = voterRepository.save(voter);
        log.info("Voter with ID {} has been unblocked", id);
        return voterMapper.toDto(updatedVoter);
    }

    public Voter getActiveVoterOrThrow(Long voterId) {
        return voterRepository.findByIdAndBlockedIsFalse(voterId)
                .orElseThrow(() -> new VoterBlockedException(
                        String.format("Voter with ID %d is blocked or not found", voterId)
                ));
    }

    public VoterDto getVoterById(Long id) {
        log.debug("Fetching voter with ID: {}", id);
        Voter voter = voterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Voter with ID %d not found", id)
                ));
        return voterMapper.toDto(voter);
    }


    public List<VoterDto> getAllVoters() {
        log.debug("Fetching all voters");
        return voterRepository.findAll().stream()
                .map(voterMapper::toDto)
                .collect(Collectors.toList());
    }
}
