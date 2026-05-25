package com.ticketing.dto.request;

import lombok.*;

// TODO: implemented in Phase X
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTicketRequest {
    private String title;
    private String description;
    private String category;
}
