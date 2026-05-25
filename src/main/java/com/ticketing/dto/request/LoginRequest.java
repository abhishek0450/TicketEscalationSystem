package com.ticketing.dto.request;

import lombok.*;

// TODO: implemented in Phase X
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {
    private String username;
    private String password;
}
