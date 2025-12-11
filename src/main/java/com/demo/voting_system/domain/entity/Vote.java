package com.demo.voting_system.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "votes", uniqueConstraints = {
        @UniqueConstraint(
                name = "unique_voter_election",
                columnNames = {"voter_id", "election_id"}
        )
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "voter_id")
    private Voter voter;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "election_id")
    private Election election;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "election_option_id")
    private ElectionOption option;

    @Column(name = "voted_at", nullable = false, updatable = false)
    private OffsetDateTime votedAt;

    @PrePersist
    protected void onCreate() {
        votedAt = OffsetDateTime.now();
    }
}
