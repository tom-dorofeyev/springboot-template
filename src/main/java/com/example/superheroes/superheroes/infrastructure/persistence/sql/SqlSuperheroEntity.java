package com.example.superheroes.superheroes.infrastructure.persistence.sql;

import com.example.superheroes.superheroes.core.model.Superhero;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "superheroes")
public class SqlSuperheroEntity {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Id
    @Column(columnDefinition = "UUID")
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String alias;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String powers;

    @Column(nullable = false)
    private int strengthLevel;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    protected SqlSuperheroEntity() {}

    private SqlSuperheroEntity(UUID id, String name, String alias, String powers,
            int strengthLevel, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.alias = alias;
        this.powers = powers;
        this.strengthLevel = strengthLevel;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static SqlSuperheroEntity from(Superhero superhero) {
        try {
            String powersJson = objectMapper.writeValueAsString(superhero.getPowers());
            return new SqlSuperheroEntity(
                    superhero.getId(), superhero.getName(), superhero.getAlias(),
                    powersJson, superhero.getStrengthLevel(),
                    superhero.getCreatedAt(), superhero.getUpdatedAt());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize powers", e);
        }
    }

    public Superhero toDomain() {
        try {
            List<String> powersList = objectMapper.readValue(powers, new TypeReference<>() {});
            return Superhero.reconstitute(id, name, alias, powersList, strengthLevel, createdAt, updatedAt);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize powers", e);
        }
    }

    public UUID getId() { return id; }

    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getAlias() { return alias; }

    public void setAlias(String alias) { this.alias = alias; }

    public String getPowers() { return powers; }

    public void setPowers(String powers) { this.powers = powers; }

    public int getStrengthLevel() { return strengthLevel; }

    public void setStrengthLevel(int strengthLevel) { this.strengthLevel = strengthLevel; }

    public Instant getCreatedAt() { return createdAt; }

    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }

    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
