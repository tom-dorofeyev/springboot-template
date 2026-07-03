package com.example.superheroes.superheroes.core.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Superhero {
    private static final int NAME_MAX_LENGTH = 100;
    private static final int ALIAS_MAX_LENGTH = 100;
    private static final int STRENGTH_MIN = 1;
    private static final int STRENGTH_MAX = 10;

    private final UUID id;
    private String name;
    private String alias;
    private List<String> powers;
    private int strengthLevel;
    private final Instant createdAt;
    private Instant updatedAt;

    private Superhero(UUID id, String name, String alias, List<String> powers,
            int strengthLevel, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.alias = alias;
        this.powers = new ArrayList<>(powers);
        this.strengthLevel = strengthLevel;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Superhero create(String name, String alias, List<String> powers, int strengthLevel) {
        validate(name, alias, powers, strengthLevel);
        Instant now = Instant.now();
        return new Superhero(UUID.randomUUID(), name, alias, powers, strengthLevel, now, now);
    }

    public static Superhero reconstitute(UUID id, String name, String alias, List<String> powers,
            int strengthLevel, Instant createdAt, Instant updatedAt) {
        validate(name, alias, powers, strengthLevel);
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(createdAt, "createdAt must not be null");
        Objects.requireNonNull(updatedAt, "updatedAt must not be null");
        return new Superhero(id, name, alias, powers, strengthLevel, createdAt, updatedAt);
    }

    public void update(String name, String alias, List<String> powers, int strengthLevel) {
        validate(name, alias, powers, strengthLevel);
        this.name = name;
        this.alias = alias;
        this.powers = new ArrayList<>(powers);
        this.strengthLevel = strengthLevel;
        this.updatedAt = Instant.now();
    }

    private static void validate(String name, String alias, List<String> powers, int strengthLevel) {
        validateName(name);
        validateAlias(alias);
        validatePowers(powers);
        validateStrengthLevel(strengthLevel);
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        if (name.length() > NAME_MAX_LENGTH) {
            throw new IllegalArgumentException("name must not exceed " + NAME_MAX_LENGTH + " characters");
        }
    }

    private static void validateAlias(String alias) {
        if (alias == null || alias.isBlank()) {
            throw new IllegalArgumentException("alias must not be blank");
        }
        if (alias.length() > ALIAS_MAX_LENGTH) {
            throw new IllegalArgumentException("alias must not exceed " + ALIAS_MAX_LENGTH + " characters");
        }
    }

    private static void validatePowers(List<String> powers) {
        if (powers == null || powers.isEmpty()) {
            throw new IllegalArgumentException("powers must not be empty");
        }
        for (String power : powers) {
            if (power == null || power.isBlank()) {
                throw new IllegalArgumentException("each power must not be blank");
            }
        }
    }

    private static void validateStrengthLevel(int strengthLevel) {
        if (strengthLevel < STRENGTH_MIN || strengthLevel > STRENGTH_MAX) {
            throw new IllegalArgumentException("strengthLevel must be between " + STRENGTH_MIN + " and " + STRENGTH_MAX);
        }
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }

    public List<String> getPowers() {
        return Collections.unmodifiableList(powers);
    }

    public int getStrengthLevel() {
        return strengthLevel;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Superhero that)) {
            return false;
        }
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
