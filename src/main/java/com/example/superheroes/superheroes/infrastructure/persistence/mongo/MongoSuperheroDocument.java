package com.example.superheroes.superheroes.infrastructure.persistence.mongo;

import com.example.superheroes.superheroes.core.model.Superhero;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Document(collection = "superheroes")
public class MongoSuperheroDocument {

    @Id
    private UUID id;

    @Field
    private String name;

    @Field
    private String alias;

    @Field
    private List<String> powers;

    @Field
    private int strengthLevel;

    @Field
    private Instant createdAt;

    @Field
    private Instant updatedAt;

    public MongoSuperheroDocument() {}

    private MongoSuperheroDocument(UUID id, String name, String alias, List<String> powers,
            int strengthLevel, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.alias = alias;
        this.powers = powers;
        this.strengthLevel = strengthLevel;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static MongoSuperheroDocument from(Superhero superhero) {
        return new MongoSuperheroDocument(
                superhero.getId(), superhero.getName(), superhero.getAlias(),
                superhero.getPowers(), superhero.getStrengthLevel(),
                superhero.getCreatedAt(), superhero.getUpdatedAt());
    }

    public Superhero toDomain() {
        return Superhero.reconstitute(id, name, alias, powers, strengthLevel, createdAt, updatedAt);
    }

    public UUID getId() { return id; }

    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getAlias() { return alias; }

    public void setAlias(String alias) { this.alias = alias; }

    public List<String> getPowers() { return powers; }

    public void setPowers(List<String> powers) { this.powers = powers; }

    public int getStrengthLevel() { return strengthLevel; }

    public void setStrengthLevel(int strengthLevel) { this.strengthLevel = strengthLevel; }

    public Instant getCreatedAt() { return createdAt; }

    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }

    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
