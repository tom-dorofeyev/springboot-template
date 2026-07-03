package com.example.superheroes.superheroes.infrastructure.persistence.sql;

import com.example.superheroes.superheroes.core.model.Superhero;
import com.example.superheroes.superheroes.core.port.SuperheroRepository;
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
class SqlSuperheroRepositoryAdapterTest {

    @Autowired
    private SuperheroRepository repository;

    @Test
    void savesAndFindsById() {
        Superhero hero = Superhero.create("Spider-Man", "Peter Parker",
                List.of("Web-slinging", "Spider-sense"), 8);
        Superhero saved = repository.save(hero);

        Optional<Superhero> found = repository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Spider-Man");
        assertThat(found.get().getAlias()).isEqualTo("Peter Parker");
        assertThat(found.get().getPowers()).containsExactly("Web-slinging", "Spider-sense");
        assertThat(found.get().getStrengthLevel()).isEqualTo(8);
    }

    @Test
    void findsAllSavedSuperheroes() {
        repository.save(Superhero.create("Iron Man", "Tony Stark",
                List.of("Powered armor"), 9));
        repository.save(Superhero.create("Thor", "Thor Odinson",
                List.of("Lightning", "Strength"), 10));

        List<Superhero> all = repository.findAll();

        assertThat(all).hasSizeGreaterThanOrEqualTo(2);
        assertThat(all).extracting(Superhero::getName).contains("Iron Man", "Thor");
    }

    @Test
    void deletesByIdAndConfirmsAbsence() {
        Superhero hero = repository.save(Superhero.create("Hulk", "Bruce Banner",
                List.of("Super strength"), 10));

        assertThat(repository.existsById(hero.getId())).isTrue();
        repository.deleteById(hero.getId());
        assertThat(repository.existsById(hero.getId())).isFalse();
    }

    @Test
    void returnsEmptyOptionalForNonExistentId() {
        Optional<Superhero> result = repository.findById(UUID.randomUUID());

        assertThat(result).isEmpty();
    }
}
