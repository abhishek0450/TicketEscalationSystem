package com.ticketing.entity;

import jakarta.persistence.*;
import lombok.*;

// TODO: implemented in Phase X
@Entity
@Table(name = "sla_configs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SlaConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String policyName;
}
