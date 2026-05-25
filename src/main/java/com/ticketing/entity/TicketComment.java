package com.ticketing.entity;

import jakarta.persistence.*;
import lombok.*;

// TODO: implemented in Phase X
@Entity
@Table(name = "ticket_comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String commentText;
}
