package com.example.superheroes.superheroes.infrastructure.persistence.inmemory;

import static org.junit.jupiter.api.Assertions.*;

import com.example.superheroes.superheroes.core.model.Superhero;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemorySuperheroRepositoryAdapterTest {

    private static final String VALID_NAME = "Spider-Man";
    private static final String VALID_ALIAS = "Peter Parker";
    private static final List<String> VALID_POWERS = List.of("Web-slinging", "Spider-sense");
    private static final int VALID_STRENGTH = 7;

    private InMemorySuperheroRepositoryAdapter repository;
    private Superhero hero;

    @BeforeEach
    void setUp() {
        repository = new InMemorySuperheroRepositoryAdapter();
        hero = Superhero.create(VALID_NAME, VALID_ALIAS, VALID_POWERS, VALID_STRENGTH);
    }

    @Test
    void saveAndFindByIdRoundTripReturnsSameHero() {
        repository.save(hero);
        Optional<Superhero> found = repository.findById(hero.getId());

        assertTrue(found.isPresent());
        assertEquals(hero.getId(), found.get().getId());
        assertEquals(VALID_NAME, found.get().getName());
        assertEquals(VALID_ALIAS, found.get().getAlias());
        assertEquals(VALID_POWERS, found.get().getPowers());
        assertEquals(VALID_STRENGTH, found.get().getStrengthLevel());
    }

    @Test
    void findByIdReturnsEmptyForNonExistentId() {
        Optional<Superhero> found = repository.findById(UUID.randomUUID());

        assertTrue(found.isEmpty());
    }

    @Test
    void findAllReturnsAllSavedHeroes() {
        Superhero second = Superhero.create("Iron Man", "Tony Stark",
            List.of("Powered armor"), 9);
        repository.save(hero);
        repository.save(second);

        List<Superhero> all = repository.findAll();

        assertEquals(2, all.size());
        assertTrue(all.contains(hero));
        assertTrue(all.contains(second));
    }

    @Test
    void findAllReturnsEmptyWhenNoHeroesSaved() {
        List<Superhero> all = repository.findAll();

        assertTrue(all.isEmpty());
    }

    @Test
    void deleteByIdRemovesHero() {
        repository.save(hero);
        repository.deleteById(hero.getId());

        assertTrue(repository.findById(hero.getId()).isEmpty());
    }

    @Test
    void deleteByIdDoesNothingForNonExistentId() {
        repository.save(hero);
        repository.deleteById(UUID.randomUUID());

        assertTrue(repository.findById(hero.getId()).isPresent());
    }

    @Test
    void existsByIdReturnsTrueForSavedHero() {
        repository.save(hero);

        assertTrue(repository.existsById(hero.getId()));
    }

    @Test
    void existsByIdReturnsFalseForNonExistentId() {
        assertFalse(repository.existsById(UUID.randomUUID()));
    }
}
