package com.example.superheroes.villains.infrastructure.persistence.sql;

import com.example.superheroes.villains.core.model.Villain;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "villains")
public class SqlVillainEntity {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Id
    @Column(columnDefinition = "UUID")
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "villain_name", nullable = false, length = 100)
    private String villainName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String powers;

    @Column(nullable = false)
    private int dangerLevel;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    protected SqlVillainEntity() {}

    private SqlVillainEntity(UUID id, String name, String villainName, String powers,
            int dangerLevel, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.villainName = villainName;
        this.powers = powers;
        this.dangerLevel = dangerLevel;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static SqlVillainEntity from(Villain villain) {
        try {
            String powersJson = objectMapper.writeValueAsString(villain.getPowers());
            return new SqlVillainEntity(
                    villain.getId(), villain.getName(), villain.getVillainName(),
                    powersJson, villain.getDangerLevel(),
                    villain.getCreatedAt(), villain.getUpdatedAt());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize powers", e);
        }
    }

    public Villain toDomain() {
        try {
            List<String> powersList = objectMapper.readValue(powers, new TypeReference<>() {});
            return Villain.reconstitute(id, name, villainName, powersList, dangerLevel, createdAt, updatedAt);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize powers", e);
        }
    }

    public UUID getId() { return id; }

    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getVillainName() { return villainName; }

    public void setVillainName(String villainName) { this.villainName = villainName; }

    public String getPowers() { return powers; }

    public void setPowers(String powers) { this.powers = powers; }

    public int getDangerLevel() { return dangerLevel; }

    public void setDangerLevel(int dangerLevel) { this.dangerLevel = dangerLevel; }

    public Instant getCreatedAt() { return createdAt; }

    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }

    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}