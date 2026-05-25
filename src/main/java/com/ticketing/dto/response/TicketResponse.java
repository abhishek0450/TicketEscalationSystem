package com.ticketing.dto.response;

import lombok.*;

// TODO: implemented in Phase X
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketResponse {
    private Long id;
    private String title;
    private String description;
    private String status;
}
