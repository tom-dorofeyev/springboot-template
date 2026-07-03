package com.example.superheroes.villains.infrastructure.persistence.mongo;

import com.example.superheroes.villains.core.model.Villain;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Document(collection = "villains")
public class MongoVillainDocument {

    @Id
    private UUID id;

    @Field
    private String name;

    @Field
    private String villainName;

    @Field
    private List<String> powers;

    @Field
    private int dangerLevel;

    @Field
    private Instant createdAt;

    @Field
    private Instant updatedAt;

    public MongoVillainDocument() {}

    private MongoVillainDocument(UUID id, String name, String villainName, List<String> powers,
            int dangerLevel, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.villainName = villainName;
        this.powers = powers;
        this.dangerLevel = dangerLevel;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static MongoVillainDocument from(Villain villain) {
        return new MongoVillainDocument(
                villain.getId(), villain.getName(), villain.getVillainName(),
                villain.getPowers(), villain.getDangerLevel(),
                villain.getCreatedAt(), villain.getUpdatedAt());
    }

    public Villain toDomain() {
        return Villain.reconstitute(id, name, villainName, powers, dangerLevel, createdAt, updatedAt);
    }

    public UUID getId() { return id; }

    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getVillainName() { return villainName; }

    public void setVillainName(String villainName) { this.villainName = villainName; }

    public List<String> getPowers() { return powers; }

    public void setPowers(List<String> powers) { this.powers = powers; }

    public int getDangerLevel() { return dangerLevel; }

    public void setDangerLevel(int dangerLevel) { this.dangerLevel = dangerLevel; }

    public Instant getCreatedAt() { return createdAt; }

    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }

    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}