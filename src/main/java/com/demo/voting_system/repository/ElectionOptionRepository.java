package com.demo.voting_system.repository;

import com.demo.voting_system.domain.entity.ElectionOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElectionOptionRepository extends JpaRepository<ElectionOption, Long> {
}
