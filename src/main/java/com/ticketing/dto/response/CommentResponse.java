package com.ticketing.dto.response;

import lombok.*;

// TODO: implemented in Phase X
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {
    private Long id;
    private String commentText;
    private String username;
}
