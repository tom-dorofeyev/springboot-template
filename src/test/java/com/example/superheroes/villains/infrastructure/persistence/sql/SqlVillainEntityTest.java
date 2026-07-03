package com.example.superheroes.villains.infrastructure.persistence.sql;

import com.example.superheroes.villains.core.model.Villain;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SqlVillainEntityTest {

    private static final String NAME = "Loki";
    private static final String VILLAIN_NAME = "God of Mischief";
    private static final List<String> POWERS = List.of("Illusions", "Shapeshifting");
    private static final int DANGER_LEVEL = 8;

    @Test
    void convertsDomainToEntityWithPowersSerializedAsJson() {
        Villain villain = Villain.create(NAME, VILLAIN_NAME, POWERS, DANGER_LEVEL);
        SqlVillainEntity entity = SqlVillainEntity.from(villain);

        assertThat(entity.getId()).isEqualTo(villain.getId());
        assertThat(entity.getName()).isEqualTo(NAME);
        assertThat(entity.getVillainName()).isEqualTo(VILLAIN_NAME);
        assertThat(entity.getPowers()).contains("Illusions");
        assertThat(entity.getPowers()).contains("Shapeshifting");
        assertThat(entity.getDangerLevel()).isEqualTo(DANGER_LEVEL);
        assertThat(entity.getCreatedAt()).isEqualTo(villain.getCreatedAt());
        assertThat(entity.getUpdatedAt()).isEqualTo(villain.getUpdatedAt());
    }

    @Test
    void convertsEntityBackToDomainDeserializingPowers() {
        Villain villain = Villain.create(NAME, VILLAIN_NAME, POWERS, DANGER_LEVEL);
        SqlVillainEntity entity = SqlVillainEntity.from(villain);
        Villain reconstituted = entity.toDomain();

        assertThat(reconstituted.getId()).isEqualTo(villain.getId());
        assertThat(reconstituted.getName()).isEqualTo(NAME);
        assertThat(reconstituted.getVillainName()).isEqualTo(VILLAIN_NAME);
        assertThat(reconstituted.getPowers()).containsExactly("Illusions", "Shapeshifting");
        assertThat(reconstituted.getDangerLevel()).isEqualTo(DANGER_LEVEL);
        assertThat(reconstituted.getCreatedAt()).isEqualTo(villain.getCreatedAt());
        assertThat(reconstituted.getUpdatedAt()).isEqualTo(villain.getUpdatedAt());
    }

    @Test
    void roundTripPreservesAllFields() {
        Villain original = Villain.create(NAME, VILLAIN_NAME, POWERS, DANGER_LEVEL);
        SqlVillainEntity entity = SqlVillainEntity.from(original);
        Villain restored = entity.toDomain();

        assertThat(restored.getId()).isEqualTo(original.getId());
        assertThat(restored.getName()).isEqualTo(original.getName());
        assertThat(restored.getVillainName()).isEqualTo(original.getVillainName());
        assertThat(restored.getPowers()).isEqualTo(original.getPowers());
        assertThat(restored.getDangerLevel()).isEqualTo(original.getDangerLevel());
        assertThat(restored.getCreatedAt()).isEqualTo(original.getCreatedAt());
        assertThat(restored.getUpdatedAt()).isEqualTo(original.getUpdatedAt());
    }
}