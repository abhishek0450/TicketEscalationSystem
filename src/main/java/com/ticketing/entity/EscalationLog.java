package com.ticketing.entity;

import jakarta.persistence.*;
import lombok.*;

// TODO: implemented in Phase X
@Entity
@Table(name = "escalation_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EscalationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String logMessage;
}
