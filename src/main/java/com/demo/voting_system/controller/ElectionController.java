package com.demo.voting_system.controller;

import com.demo.voting_system.domain.dto.ElectionDto;
import com.demo.voting_system.domain.entity.Election;
import com.demo.voting_system.service.ElectionService;
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
@RequestMapping("/api/v1/elections")
public class ElectionController {
    private final ElectionService electionService;

    @PostMapping
    public ResponseEntity<ElectionDto> addElection(@Valid @RequestBody ElectionDto electionDto) {
        log.info("POST /api/v1/elections - Creating new election");
        ElectionDto createdElection = electionService.addElection(electionDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdElection);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ElectionDto>> getAllElections() {
        log.info("GET /api/v1/elections/all - Fetching all elections");
        List<ElectionDto> elections = electionService.getAllElections();
        return ResponseEntity.ok(elections);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ElectionDto> getElection(@PathVariable Long id) {
        log.info("GET /api/v1/elections/{} - Fetching election", id);
        ElectionDto election = electionService.getElection(id);
        return ResponseEntity.ok(election);
    }

    @GetMapping("/active")
    public ResponseEntity<List<ElectionDto>> getActiveElections() {
        log.info("GET /api/v1/elections/active - Fetching active elections");
        List<ElectionDto> elections = electionService.getActiveElections();
        return ResponseEntity.ok(elections);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ElectionDto> updateElectionStatus(
            @PathVariable Long id,
            @RequestParam Election.ElectionStatus status) {
        log.info("PUT /api/v1/elections/{}/status - Updating election status to {}", id, status);
        ElectionDto updatedElection = electionService.updateElectionStatus(id, status);
        return ResponseEntity.ok(updatedElection);
    }
}
