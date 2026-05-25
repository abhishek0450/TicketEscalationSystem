package com.ticketing.dto.response;

import lombok.*;

// TODO: implemented in Phase X
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStatsResponse {
    private long totalTickets;
    private long openTickets;
    private long escalatedTickets;
}
