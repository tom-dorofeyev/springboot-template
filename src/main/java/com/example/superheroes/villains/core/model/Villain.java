package com.example.superheroes.villains.core.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Villain {
    private static final int NAME_MAX_LENGTH = 100;
    private static final int VILLAIN_NAME_MAX_LENGTH = 100;
    private static final int DANGER_LEVEL_MIN = 1;
    private static final int DANGER_LEVEL_MAX = 10;

    private final UUID id;
    private String name;
    private String villainName;
    private List<String> powers;
    private int dangerLevel;
    private final Instant createdAt;
    private Instant updatedAt;

    private Villain(UUID id, String name, String villainName, List<String> powers,
            int dangerLevel, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.villainName = villainName;
        this.powers = new ArrayList<>(powers);
        this.dangerLevel = dangerLevel;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Villain create(String name, String villainName, List<String> powers, int dangerLevel) {
        validate(name, villainName, powers, dangerLevel);
        Instant now = Instant.now();
        return new Villain(UUID.randomUUID(), name, villainName, powers, dangerLevel, now, now);
    }

    public static Villain reconstitute(UUID id, String name, String villainName, List<String> powers,
            int dangerLevel, Instant createdAt, Instant updatedAt) {
        validate(name, villainName, powers, dangerLevel);
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(createdAt, "createdAt must not be null");
        Objects.requireNonNull(updatedAt, "updatedAt must not be null");
        return new Villain(id, name, villainName, powers, dangerLevel, createdAt, updatedAt);
    }

    public void update(String name, String villainName, List<String> powers, int dangerLevel) {
        validate(name, villainName, powers, dangerLevel);
        this.name = name;
        this.villainName = villainName;
        this.powers = new ArrayList<>(powers);
        this.dangerLevel = dangerLevel;
        this.updatedAt = Instant.now();
    }

    private static void validate(String name, String villainName, List<String> powers, int dangerLevel) {
        validateName(name);
        validateVillainName(villainName);
        validatePowers(powers);
        validateDangerLevel(dangerLevel);
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        if (name.length() > NAME_MAX_LENGTH) {
            throw new IllegalArgumentException("name must not exceed " + NAME_MAX_LENGTH + " characters");
        }
    }

    private static void validateVillainName(String villainName) {
        if (villainName == null || villainName.isBlank()) {
            throw new IllegalArgumentException("villainName must not be blank");
        }
        if (villainName.length() > VILLAIN_NAME_MAX_LENGTH) {
            throw new IllegalArgumentException("villainName must not exceed " + VILLAIN_NAME_MAX_LENGTH + " characters");
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

    private static void validateDangerLevel(int dangerLevel) {
        if (dangerLevel < DANGER_LEVEL_MIN || dangerLevel > DANGER_LEVEL_MAX) {
            throw new IllegalArgumentException("dangerLevel must be between " + DANGER_LEVEL_MIN + " and " + DANGER_LEVEL_MAX);
        }
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVillainName() {
        return villainName;
    }

    public List<String> getPowers() {
        return Collections.unmodifiableList(powers);
    }

    public int getDangerLevel() {
        return dangerLevel;
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
        if (!(o instanceof Villain that)) {
            return false;
        }
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
