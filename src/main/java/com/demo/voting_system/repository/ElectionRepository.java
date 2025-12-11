package com.demo.voting_system.repository;

import com.demo.voting_system.domain.entity.Election;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface ElectionRepository extends JpaRepository<Election, Long> {
    List<Election> findAllByStatusAndStartDateBeforeAndEndDateAfter(
            Election.ElectionStatus status,
            OffsetDateTime now1,
            OffsetDateTime now2
    );
}
