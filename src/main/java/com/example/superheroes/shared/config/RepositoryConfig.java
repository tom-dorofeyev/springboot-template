package com.example.superheroes.shared.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {
    // No explicit bean definitions needed.
    // Each adapter is annotated with @Profile("sql"|"mongo"|"inmemory") and @Repository.
    // Spring auto-picks the one matching the active profile.
    // Default profile is "inmemory" (set in application.properties).
}
