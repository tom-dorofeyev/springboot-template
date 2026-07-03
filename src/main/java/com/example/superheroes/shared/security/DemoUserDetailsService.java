package com.example.superheroes.shared.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DemoUserDetailsService implements UserDetailsService {

    // NOTE: DO NOT USE HARDCODED USERS IN PRODUCTION
    private static final Map<String, UserDetails> USERS = Map.of(
        "admin", User.withUsername("admin")
            .password("admin")
            .roles("ADMIN")
            .build(),
        "user", User.withUsername("user")
            .password("user")
            .roles("USER")
            .build()
    );

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = USERS.get(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return user;
    }
}
