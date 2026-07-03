package com.example.superheroes.superheroes.application.dto;

import com.example.superheroes.superheroes.core.model.Superhero;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record SuperheroResponse(
    UUID id,
    String name,
    String alias,
    List<String> powers,
    int strengthLevel,
    Instant createdAt,
    Instant updatedAt
) {
    public static SuperheroResponse from(Superhero superhero) {
        return new SuperheroResponse(
            superhero.getId(),
            superhero.getName(),
            superhero.getAlias(),
            superhero.getPowers(),
            superhero.getStrengthLevel(),
            superhero.getCreatedAt(),
            superhero.getUpdatedAt()
        );
    }
}
