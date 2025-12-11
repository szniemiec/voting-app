package com.demo.voting_system.controller;

import com.demo.voting_system.domain.dto.VoterDto;
import com.demo.voting_system.service.VoterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/voters")
public class VoterController {
    private final VoterService voterService;

    @PostMapping
    public ResponseEntity<VoterDto> addVoter(@Valid @RequestBody VoterDto voterDto) {
        log.info("POST /api/v1/voters - Creating new voter");
        VoterDto createdVoter = voterService.addVoter(voterDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdVoter);
    }

    @GetMapping("/all")
    public ResponseEntity<List<VoterDto>> getAllVoters() {
        log.info("GET /api/v1/voters/all - Fetching all voters");
        List<VoterDto> voters = voterService.getAllVoters();
        return ResponseEntity.ok(voters);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VoterDto> getVoter(@PathVariable Long id) {
        log.info("GET /api/v1/voters/{} - Fetching voter", id);
        VoterDto voter = voterService.getVoterById(id);
        return ResponseEntity.ok(voter);
    }

    @PutMapping("/{id}/block")
    public ResponseEntity<VoterDto> blockVoter(@PathVariable Long id) {
        log.info("PUT /api/v1/voters/{}/block - Blocking voter", id);
        VoterDto blockedVoter = voterService.blockVoter(id);
        return ResponseEntity.ok(blockedVoter);
    }

    @PutMapping("/{id}/unblock")
    public ResponseEntity<VoterDto> unblockVoter(@PathVariable Long id) {
        log.info("PUT /api/v1/voters/{}/unblock - Unblocking voter", id);
        VoterDto unblockedVoter = voterService.unblockVoter(id);
        return ResponseEntity.ok(unblockedVoter);
    }
}
