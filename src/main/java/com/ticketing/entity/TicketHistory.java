package com.ticketing.entity;

import jakarta.persistence.*;
import lombok.*;

// TODO: implemented in Phase X
@Entity
@Table(name = "ticket_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String changeDetails;
}
