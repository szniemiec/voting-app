package com.demo.voting_system.repository;

import com.demo.voting_system.domain.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    @Query("SELECT v FROM Vote v WHERE v.voter.id = :voterId AND v.election.id = :electionId")
    Optional<Vote> findByVoterAndElection(Long voterId, Long electionId);

    boolean existsByVoterIdAndElectionId(Long voterId, Long electionId);
}
