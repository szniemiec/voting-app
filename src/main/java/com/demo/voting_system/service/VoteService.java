package com.demo.voting_system.service;

import com.demo.voting_system.domain.dto.VoteDto;
import com.demo.voting_system.domain.dto.VoteResponseDto;
import com.demo.voting_system.domain.entity.Election;
import com.demo.voting_system.domain.entity.ElectionOption;
import com.demo.voting_system.domain.entity.Vote;
import com.demo.voting_system.domain.entity.Voter;
import com.demo.voting_system.exception.ElectionNotActiveException;
import com.demo.voting_system.exception.ResourceNotFoundException;
import com.demo.voting_system.exception.VoterAlreadyVotedException;
import com.demo.voting_system.exception.VotingSystemException;
import com.demo.voting_system.repository.ElectionOptionRepository;
import com.demo.voting_system.repository.VoteRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final VoterService voterService;
    private final ElectionService electionService;
    private final ElectionOptionRepository electionOptionRepository;

    @Transactional
    public VoteResponseDto addVote(VoteDto voteDto) {
        log.info("Processing vote for voter: {}, election: {}",
                voteDto.voterId(), voteDto.electionId());

        Voter voter = voterService.getActiveVoterOrThrow(voteDto.voterId());
        Election election = electionService.getElectionById(voteDto.electionId());
        ElectionOption option = electionOptionRepository.findById(voteDto.optionId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Option with ID %d not found", voteDto.optionId())
                ));

        validateVote(voteDto, election, option);

        Vote vote = Vote.builder()
                .voter(voter)
                .election(election)
                .option(option)
                .build();
        Vote savedVote = voteRepository.save(vote);

        log.info("Vote recorded successfully. Vote ID: {}", savedVote.getId());

        return VoteResponseDto.builder()
                .id(savedVote.getId())
                .voterId(savedVote.getVoter().getId())
                .electionId(savedVote.getElection().getId())
                .optionId(savedVote.getOption().getId())
                .votedAt(savedVote.getVotedAt())
                .message("Vote recorded successfully")
                .build();
    }

    public List<VoteResponseDto> getElectionResults(Long electionId) {
        log.debug("Fetching election results for election: {}", electionId);

        electionService.getElectionById(electionId);

        return voteRepository.findAll().stream()
                .filter(vote -> vote.getElection()
                        .getId().equals(electionId))
                .map(this::toVoteResponseDto)
                .collect(Collectors.toList());
    }

    private void validateVote(VoteDto voteDto, Election election, ElectionOption option) {
        // Check if election is active
        if (!electionService.isElectionActive(election)) {
            log.warn("Election {} is not active", voteDto.electionId());
            throw new ElectionNotActiveException(
                    String.format("Election with ID %d is not active", voteDto.electionId())
            );
        }

        // Check if voter already voted in this election
        if (voteRepository.existsByVoterIdAndElectionId(voteDto.voterId(), voteDto.electionId())) {
            log.warn("Voter {} already voted in election {}", voteDto.voterId(), voteDto.electionId());
            throw new VoterAlreadyVotedException(
                    String.format("Voter has already voted in election %d", voteDto.electionId())
            );
        }

        // Verify option belongs to election
        if (!option.getElection().getId().equals(voteDto.electionId())) {
            log.warn("Option {} does not belong to election {}", voteDto.optionId(), voteDto.electionId());
            throw new VotingSystemException(
                    "Selected option does not belong to this election"
            );
        }
    }

    private VoteResponseDto toVoteResponseDto(Vote vote) {
        return VoteResponseDto.builder()
                .id(vote.getId())
                .voterId(vote.getVoter().getId())
                .electionId(vote.getElection().getId())
                .optionId(vote.getOption().getId())
                .votedAt(vote.getVotedAt())
                .build();
    }
}
