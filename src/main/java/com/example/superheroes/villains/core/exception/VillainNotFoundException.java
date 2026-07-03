package com.example.superheroes.villains.core.exception;

import java.util.UUID;

public class VillainNotFoundException extends RuntimeException {
    public VillainNotFoundException(UUID id) {
        super("Villain not found with id: " + id);
    }
}
