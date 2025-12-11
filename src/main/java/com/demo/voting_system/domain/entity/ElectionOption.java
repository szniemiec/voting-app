package com.demo.voting_system.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "election_options")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ElectionOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "election_id")
    private Election election;

    @Column(nullable = false)
    @EqualsAndHashCode.Include
    private String label;

    @Column(columnDefinition = "TEXT")
    private String description;
}
