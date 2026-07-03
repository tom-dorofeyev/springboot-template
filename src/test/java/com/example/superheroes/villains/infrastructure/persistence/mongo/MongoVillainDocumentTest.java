package com.example.superheroes.villains.infrastructure.persistence.mongo;

import com.example.superheroes.villains.core.model.Villain;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MongoVillainDocumentTest {

    @Test
    void fromMapsAllFields() {
        Villain villain = Villain.create("Loki", "God of Mischief", List.of("Illusions", "Shapeshifting"), 8);
        MongoVillainDocument doc = MongoVillainDocument.from(villain);

        assertThat(doc.getId()).isEqualTo(villain.getId());
        assertThat(doc.getName()).isEqualTo("Loki");
        assertThat(doc.getVillainName()).isEqualTo("God of Mischief");
        assertThat(doc.getPowers()).containsExactly("Illusions", "Shapeshifting");
        assertThat(doc.getDangerLevel()).isEqualTo(8);
        assertThat(doc.getCreatedAt()).isEqualTo(villain.getCreatedAt());
        assertThat(doc.getUpdatedAt()).isEqualTo(villain.getUpdatedAt());
    }

    @Test
    void toDomainReconstitutesCorrectly() {
        Villain villain = Villain.create("Thanos", "The Mad Titan", List.of("Infinity Gauntlet", "Super strength"), 10);
        MongoVillainDocument doc = MongoVillainDocument.from(villain);
        Villain reconstituted = doc.toDomain();

        assertThat(reconstituted.getId()).isEqualTo(villain.getId());
        assertThat(reconstituted.getName()).isEqualTo(villain.getName());
        assertThat(reconstituted.getPowers()).containsExactlyElementsOf(villain.getPowers());
    }

    @Test
    void roundTripPreservesAllFields() {
        Villain original = Villain.create("Ultron", "The Machine", List.of("AI", "Robotics"), 9);
        MongoVillainDocument doc = MongoVillainDocument.from(original);
        Villain back = doc.toDomain();

        assertThat(back.getId()).isEqualTo(original.getId());
        assertThat(back.getName()).isEqualTo(original.getName());
        assertThat(back.getVillainName()).isEqualTo(original.getVillainName());
        assertThat(back.getPowers()).containsExactlyElementsOf(original.getPowers());
        assertThat(back.getDangerLevel()).isEqualTo(original.getDangerLevel());
        assertThat(back.getCreatedAt()).isEqualTo(original.getCreatedAt());
        assertThat(back.getUpdatedAt()).isEqualTo(original.getUpdatedAt());
    }
}