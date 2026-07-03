package com.example.superheroes.superheroes.core.exception;

import java.util.UUID;

public class SuperheroNotFoundException extends RuntimeException {
    public SuperheroNotFoundException(UUID id) {
        super("Superhero not found with id: " + id);
    }
}
