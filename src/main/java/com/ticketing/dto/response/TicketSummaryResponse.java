package com.ticketing.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketSummaryResponse {
    private Long id;
    private String title;
    private String status;
    private String priority;
    private String categoryName;
    private UserSummaryResponse createdBy;
    private UserSummaryResponse assignedAgent;
    private Integer escalationLevel;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime dueAt;
}
