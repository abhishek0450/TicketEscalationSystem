package com.ticketing.dto.request;

import lombok.*;

// TODO: implemented in Phase X
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddCommentRequest {
    private String commentText;
}
