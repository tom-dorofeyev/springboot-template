package com.example.superheroes.shared.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenProviderTest {

    private final JwtTokenProvider provider = new JwtTokenProvider();

    @Test
    void createsAndValidatesToken() {
        Authentication auth = new UsernamePasswordAuthenticationToken(
            "admin", "password", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        String token = provider.createToken(auth);
        assertThat(token).isNotBlank();
        assertThat(provider.validateToken(token)).isTrue();
    }

    @Test
    void rejectsInvalidToken() {
        assertThat(provider.validateToken("invalid.token.here")).isFalse();
    }

    @Test
    void extractsAuthenticationFromToken() {
        Authentication auth = new UsernamePasswordAuthenticationToken(
            "user", "password", List.of(new SimpleGrantedAuthority("ROLE_USER")));
        String token = provider.createToken(auth);
        Authentication extracted = provider.getAuthentication(token);
        assertThat(extracted.getName()).isEqualTo("user");
        assertThat(extracted.getAuthorities())
            .extracting("authority")
            .contains("ROLE_USER");
    }

    @Test
    void extractsUsernameFromToken() {
        Authentication auth = new UsernamePasswordAuthenticationToken(
            "batman", "password", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        String token = provider.createToken(auth);
        assertThat(provider.getUsernameFromToken(token)).isEqualTo("batman");
    }
}
