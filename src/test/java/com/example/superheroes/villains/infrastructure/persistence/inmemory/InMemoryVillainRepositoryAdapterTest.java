package com.example.superheroes.villains.infrastructure.persistence.inmemory;

import static org.junit.jupiter.api.Assertions.*;

import com.example.superheroes.villains.core.model.Villain;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryVillainRepositoryAdapterTest {

    private static final String VALID_NAME = "Loki";
    private static final String VALID_VILLAIN_NAME = "God of Mischief";
    private static final List<String> VALID_POWERS = List.of("Illusions", "Shapeshifting");
    private static final int VALID_DANGER_LEVEL = 8;

    private InMemoryVillainRepositoryAdapter repository;
    private Villain villain;

    @BeforeEach
    void setUp() {
        repository = new InMemoryVillainRepositoryAdapter();
        villain = Villain.create(VALID_NAME, VALID_VILLAIN_NAME, VALID_POWERS, VALID_DANGER_LEVEL);
    }

    @Test
    void saveAndFindByIdRoundTripReturnsSameVillain() {
        repository.save(villain);
        Optional<Villain> found = repository.findById(villain.getId());

        assertTrue(found.isPresent());
        assertEquals(villain.getId(), found.get().getId());
        assertEquals(VALID_NAME, found.get().getName());
        assertEquals(VALID_VILLAIN_NAME, found.get().getVillainName());
        assertEquals(VALID_POWERS, found.get().getPowers());
        assertEquals(VALID_DANGER_LEVEL, found.get().getDangerLevel());
    }

    @Test
    void findByIdReturnsEmptyForNonExistentId() {
        Optional<Villain> found = repository.findById(UUID.randomUUID());

        assertTrue(found.isEmpty());
    }

    @Test
    void findAllReturnsAllSavedVillains() {
        Villain second = Villain.create("Thanos", "The Mad Titan",
            List.of("Infinity Gauntlet"), 10);
        repository.save(villain);
        repository.save(second);

        List<Villain> all = repository.findAll();

        assertEquals(2, all.size());
        assertTrue(all.contains(villain));
        assertTrue(all.contains(second));
    }

    @Test
    void findAllReturnsEmptyWhenNoVillainsSaved() {
        List<Villain> all = repository.findAll();

        assertTrue(all.isEmpty());
    }

    @Test
    void deleteByIdRemovesVillain() {
        repository.save(villain);
        repository.deleteById(villain.getId());

        assertTrue(repository.findById(villain.getId()).isEmpty());
    }

    @Test
    void deleteByIdDoesNothingForNonExistentId() {
        repository.save(villain);
        repository.deleteById(UUID.randomUUID());

        assertTrue(repository.findById(villain.getId()).isPresent());
    }

    @Test
    void existsByIdReturnsTrueForSavedVillain() {
        repository.save(villain);

        assertTrue(repository.existsById(villain.getId()));
    }

    @Test
    void existsByIdReturnsFalseForNonExistentId() {
        assertFalse(repository.existsById(UUID.randomUUID()));
    }
}