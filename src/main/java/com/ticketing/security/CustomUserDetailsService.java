package com.ticketing.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// TODO: implemented in Phase X
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Return dummy or throw UsernameNotFoundException for skeleton
        throw new UsernameNotFoundException("User not found: " + username);
    }
}
