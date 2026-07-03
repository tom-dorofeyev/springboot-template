package com.example.superheroes.superheroes.infrastructure.persistence.sql;

import com.example.superheroes.superheroes.core.model.Superhero;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SqlSuperheroEntityTest {

    private static final String NAME = "Spider-Man";
    private static final String ALIAS = "Peter Parker";
    private static final List<String> POWERS = List.of("Web-slinging", "Spider-sense");
    private static final int STRENGTH_LEVEL = 8;

    @Test
    void convertsDomainToEntityWithPowersSerializedAsJson() {
        Superhero hero = Superhero.create(NAME, ALIAS, POWERS, STRENGTH_LEVEL);
        SqlSuperheroEntity entity = SqlSuperheroEntity.from(hero);

        assertThat(entity.getId()).isEqualTo(hero.getId());
        assertThat(entity.getName()).isEqualTo(NAME);
        assertThat(entity.getAlias()).isEqualTo(ALIAS);
        assertThat(entity.getPowers()).contains("Web-slinging");
        assertThat(entity.getPowers()).contains("Spider-sense");
        assertThat(entity.getStrengthLevel()).isEqualTo(STRENGTH_LEVEL);
        assertThat(entity.getCreatedAt()).isEqualTo(hero.getCreatedAt());
        assertThat(entity.getUpdatedAt()).isEqualTo(hero.getUpdatedAt());
    }

    @Test
    void convertsEntityBackToDomainDeserializingPowers() {
        Superhero hero = Superhero.create(NAME, ALIAS, POWERS, STRENGTH_LEVEL);
        SqlSuperheroEntity entity = SqlSuperheroEntity.from(hero);
        Superhero reconstituted = entity.toDomain();

        assertThat(reconstituted.getId()).isEqualTo(hero.getId());
        assertThat(reconstituted.getName()).isEqualTo(NAME);
        assertThat(reconstituted.getAlias()).isEqualTo(ALIAS);
        assertThat(reconstituted.getPowers()).containsExactly("Web-slinging", "Spider-sense");
        assertThat(reconstituted.getStrengthLevel()).isEqualTo(STRENGTH_LEVEL);
        assertThat(reconstituted.getCreatedAt()).isEqualTo(hero.getCreatedAt());
        assertThat(reconstituted.getUpdatedAt()).isEqualTo(hero.getUpdatedAt());
    }

    @Test
    void roundTripPreservesAllFields() {
        Superhero original = Superhero.create(NAME, ALIAS, POWERS, STRENGTH_LEVEL);
        SqlSuperheroEntity entity = SqlSuperheroEntity.from(original);
        Superhero restored = entity.toDomain();

        assertThat(restored.getId()).isEqualTo(original.getId());
        assertThat(restored.getName()).isEqualTo(original.getName());
        assertThat(restored.getAlias()).isEqualTo(original.getAlias());
        assertThat(restored.getPowers()).isEqualTo(original.getPowers());
        assertThat(restored.getStrengthLevel()).isEqualTo(original.getStrengthLevel());
        assertThat(restored.getCreatedAt()).isEqualTo(original.getCreatedAt());
        assertThat(restored.getUpdatedAt()).isEqualTo(original.getUpdatedAt());
    }
}
