package com.example.superheroes.superheroes.infrastructure.persistence.mongo;

import com.example.superheroes.superheroes.core.model.Superhero;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MongoSuperheroDocumentTest {

    @Test
    void fromMapsAllFields() {
        Superhero hero = Superhero.create("Batman", "Bruce Wayne", List.of("Martial arts", "Gadgets"), 7);
        MongoSuperheroDocument doc = MongoSuperheroDocument.from(hero);

        assertThat(doc.getId()).isEqualTo(hero.getId());
        assertThat(doc.getName()).isEqualTo("Batman");
        assertThat(doc.getAlias()).isEqualTo("Bruce Wayne");
        assertThat(doc.getPowers()).containsExactly("Martial arts", "Gadgets");
        assertThat(doc.getStrengthLevel()).isEqualTo(7);
        assertThat(doc.getCreatedAt()).isEqualTo(hero.getCreatedAt());
        assertThat(doc.getUpdatedAt()).isEqualTo(hero.getUpdatedAt());
    }

    @Test
    void toDomainReconstitutesCorrectly() {
        Superhero hero = Superhero.create("Wonder Woman", "Diana", List.of("Flight", "Combat"), 9);
        MongoSuperheroDocument doc = MongoSuperheroDocument.from(hero);
        Superhero reconstituted = doc.toDomain();

        assertThat(reconstituted.getId()).isEqualTo(hero.getId());
        assertThat(reconstituted.getName()).isEqualTo(hero.getName());
        assertThat(reconstituted.getPowers()).containsExactlyElementsOf(hero.getPowers());
    }

    @Test
    void roundTripPreservesAllFields() {
        Superhero original = Superhero.create("Flash", "Barry Allen", List.of("Speed"), 8);
        MongoSuperheroDocument doc = MongoSuperheroDocument.from(original);
        Superhero back = doc.toDomain();

        assertThat(back.getId()).isEqualTo(original.getId());
        assertThat(back.getName()).isEqualTo(original.getName());
        assertThat(back.getAlias()).isEqualTo(original.getAlias());
        assertThat(back.getPowers()).containsExactlyElementsOf(original.getPowers());
        assertThat(back.getStrengthLevel()).isEqualTo(original.getStrengthLevel());
        assertThat(back.getCreatedAt()).isEqualTo(original.getCreatedAt());
        assertThat(back.getUpdatedAt()).isEqualTo(original.getUpdatedAt());
    }
}
