package com.demo.voting_system.controller;

import com.demo.voting_system.domain.dto.VoteDto;
import com.demo.voting_system.domain.dto.VoteResponseDto;
import com.demo.voting_system.service.VoteService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/votes")
public class VoteController {
    private final VoteService voteService;

    @PostMapping
    public ResponseEntity<VoteResponseDto> addVote(@Valid @RequestBody VoteDto voteDto) {
        log.info("POST /api/v1/votes - Casting vote for voter: {}, election: {}",
                voteDto.voterId(), voteDto.electionId());
        VoteResponseDto vote = voteService.addVote(voteDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(vote);
    }

    @GetMapping("/election/{electionId}")
    public ResponseEntity<List<VoteResponseDto>> getElectionResults(@PathVariable @NonNull Long electionId) {
        log.info("GET /api/v1/votes/election/{} - Fetching election results", electionId);
        List<VoteResponseDto> results = voteService.getElectionResults(electionId);
        return ResponseEntity.ok(results);
    }
}
