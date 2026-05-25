package com.ticketing.security;

import org.springframework.stereotype.Component;

// TODO: implemented in Phase X
@Component
public class JwtTokenProvider {

    public String generateToken(String username) {
        return null;
    }

    public String getUsernameFromJwt(String token) {
        return null;
    }

    public boolean validateToken(String token) {
        return false;
    }
}
