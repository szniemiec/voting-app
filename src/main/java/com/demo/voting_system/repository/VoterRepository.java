package com.demo.voting_system.repository;

import com.demo.voting_system.domain.entity.Voter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoterRepository extends JpaRepository<Voter, Long> {
    Optional<Voter> findByIdAndBlockedIsFalse(Long id);

    boolean existsByPeselNumber(String peselNumber);
}
