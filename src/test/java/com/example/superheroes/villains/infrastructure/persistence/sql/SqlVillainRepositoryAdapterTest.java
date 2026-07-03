package com.example.superheroes.villains.infrastructure.persistence.sql;

import com.example.superheroes.villains.core.model.Villain;
import com.example.superheroes.villains.core.port.VillainRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles({"sql", "sql-test"})
class SqlVillainRepositoryAdapterTest {

    @Autowired
    private VillainRepository repository;

    @Test
    void savesAndFindsById() {
        Villain villain = Villain.create("Loki", "God of Mischief",
                List.of("Illusions", "Shapeshifting"), 8);
        Villain saved = repository.save(villain);

        Optional<Villain> found = repository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Loki");
        assertThat(found.get().getVillainName()).isEqualTo("God of Mischief");
        assertThat(found.get().getPowers()).containsExactly("Illusions", "Shapeshifting");
        assertThat(found.get().getDangerLevel()).isEqualTo(8);
    }

    @Test
    void findsAllSavedVillains() {
        repository.save(Villain.create("Thanos", "The Mad Titan",
                List.of("Infinity Gauntlet"), 10));
        repository.save(Villain.create("Ultron", "The Machine",
                List.of("AI", "Robotics"), 9));

        List<Villain> all = repository.findAll();

        assertThat(all).hasSizeGreaterThanOrEqualTo(2);
        assertThat(all).extracting(Villain::getName).contains("Thanos", "Ultron");
    }

    @Test
    void deletesByIdAndConfirmsAbsence() {
        Villain villain = repository.save(Villain.create("Venom", "The Symbiote",
                List.of("Bonding", "Web-slinging"), 7));

        assertThat(repository.existsById(villain.getId())).isTrue();
        repository.deleteById(villain.getId());
        assertThat(repository.existsById(villain.getId())).isFalse();
    }

    @Test
    void returnsEmptyOptionalForNonExistentId() {
        Optional<Villain> result = repository.findById(UUID.randomUUID());

        assertThat(result).isEmpty();
    }
}