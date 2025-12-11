package com.demo.voting_system.service;

import com.demo.voting_system.domain.dto.ElectionDto;
import com.demo.voting_system.domain.entity.Election;
import com.demo.voting_system.exception.ResourceNotFoundException;
import com.demo.voting_system.mapper.ElectionMapper;
import com.demo.voting_system.repository.ElectionRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ElectionService {
    private final ElectionRepository electionRepository;
    private final ElectionMapper electionMapper;

    @Transactional
    public ElectionDto addElection(ElectionDto electionDto) {
        log.info("Creating new election: {}", electionDto.title());

        Election election = electionMapper.toEntity(electionDto);

        election.getOptions()
                .forEach(option -> option.setElection(election));

        Election savedElection = electionRepository.save(election);
        log.info("Election created successfully with ID: {}", savedElection.getId());
        return electionMapper.toDto(savedElection);
    }

    @Transactional
    public ElectionDto updateElectionStatus(Long id, Election.ElectionStatus status) {
        log.info("Updating election {} status to: {}", id, status);
        Election election = getElectionById(id);
        election.setStatus(status);
        Election updatedElection = electionRepository.save(election);
        log.info("Election {} status updated to: {}", id, status);
        return electionMapper.toDto(updatedElection);
    }

    public ElectionDto getElection(Long id) {
        log.debug("Fetching election with ID: {}", id);
        Election election = getElectionById(id);
        return electionMapper.toDto(election);
    }

    public List<ElectionDto> getAllElections() {
        log.debug("Fetching all elections");
        return electionRepository.findAll().stream()
                .map(electionMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<ElectionDto> getActiveElections() {
        log.debug("Fetching active elections");
        OffsetDateTime now = OffsetDateTime.now();
        return electionRepository.findAllByStatusAndStartDateBeforeAndEndDateAfter(
                        Election.ElectionStatus.ACTIVE, now, now
                ).stream()
                .map(electionMapper::toDto)
                .collect(Collectors.toList());
    }

    public Election getElectionById(Long electionId) {
        return electionRepository.findById(electionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Election with ID %d not found", electionId)
                ));
    }

    public boolean isElectionActive(Election election) {
        OffsetDateTime now = OffsetDateTime.now();
        return election.getStatus() == Election.ElectionStatus.ACTIVE &&
                election.getStartDate().isBefore(now) &&
                election.getEndDate().isAfter(now);
    }
}
