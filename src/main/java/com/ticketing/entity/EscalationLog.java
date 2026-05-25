package com.ticketing.entity;

import com.ticketing.entity.enums.EscalationLevel;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "escalation_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EscalationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "escalation_level", nullable = false)
    private EscalationLevel escalationLevel;

    @Column(name = "escalated_at", nullable = false, updatable = false)
    private LocalDateTime escalatedAt;

    @Column(name = "notified_email")
    private String notifiedEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "escalated_to")
    private User escalatedTo;

    @PrePersist
    protected void onCreate() {
        this.escalatedAt = LocalDateTime.now();
    }
}
