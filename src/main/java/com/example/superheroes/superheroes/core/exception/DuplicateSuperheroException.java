package com.example.superheroes.superheroes.core.exception;

public class DuplicateSuperheroException extends RuntimeException {
    public DuplicateSuperheroException(String name) {
        super("Superhero with name '" + name + "' already exists");
    }
}
