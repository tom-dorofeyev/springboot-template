package com.example.superheroes.villains.application.dto;

import com.example.superheroes.villains.core.model.Villain;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record VillainResponse(
    UUID id,
    String name,
    String villainName,
    List<String> powers,
    int dangerLevel,
    Instant createdAt,
    Instant updatedAt
) {
    public static VillainResponse from(Villain villain) {
        return new VillainResponse(
            villain.getId(),
            villain.getName(),
            villain.getVillainName(),
            villain.getPowers(),
            villain.getDangerLevel(),
            villain.getCreatedAt(),
            villain.getUpdatedAt()
        );
    }
}
