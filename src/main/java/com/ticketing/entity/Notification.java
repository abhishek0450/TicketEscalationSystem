package com.ticketing.entity;

import jakarta.persistence.*;
import lombok.*;

// TODO: implemented in Phase X
@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;
}
