package com.example.superheroes.superheroes.application.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.superheroes.superheroes.application.dto.CreateSuperheroRequest;
import com.example.superheroes.superheroes.application.dto.SuperheroResponse;
import com.example.superheroes.superheroes.application.dto.UpdateSuperheroRequest;
import com.example.superheroes.superheroes.core.exception.SuperheroNotFoundException;
import com.example.superheroes.superheroes.core.model.Superhero;
import com.example.superheroes.superheroes.infrastructure.persistence.inmemory.InMemorySuperheroRepositoryAdapter;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SuperheroServiceTest {

    private static final String VALID_NAME = "Spider-Man";
    private static final String VALID_ALIAS = "Peter Parker";
    private static final List<String> VALID_POWERS = List.of("Web-slinging", "Spider-sense");
    private static final int VALID_STRENGTH = 7;

    private InMemorySuperheroRepositoryAdapter adapter;
    private SuperheroServiceImpl service;

    @BeforeEach
    void setUp() {
        adapter = new InMemorySuperheroRepositoryAdapter();
        service = new SuperheroServiceImpl(adapter);
    }

    // --- create ---

    @Test
    void create_returnsResponseWithAllFieldsMatchingRequest() {
        CreateSuperheroRequest request = new CreateSuperheroRequest(
            VALID_NAME, VALID_ALIAS, VALID_POWERS, VALID_STRENGTH);

        SuperheroResponse response = service.create(request);

        assertNotNull(response.id());
        assertEquals(VALID_NAME, response.name());
        assertEquals(VALID_ALIAS, response.alias());
        assertEquals(VALID_POWERS, response.powers());
        assertEquals(VALID_STRENGTH, response.strengthLevel());
        assertNotNull(response.createdAt());
        assertNotNull(response.updatedAt());
    }

    @Test
    void create_generatesUniqueIdForEachCreate() {
        CreateSuperheroRequest request = new CreateSuperheroRequest(
            VALID_NAME, VALID_ALIAS, VALID_POWERS, VALID_STRENGTH);

        SuperheroResponse first = service.create(request);
        SuperheroResponse second = service.create(request);

        assertNotEquals(first.id(), second.id());
    }

    @Test
    void create_roundTripsThroughGetById() {
        CreateSuperheroRequest request = new CreateSuperheroRequest(
            VALID_NAME, VALID_ALIAS, VALID_POWERS, VALID_STRENGTH);

        SuperheroResponse created = service.create(request);
        SuperheroResponse retrieved = service.getById(created.id());

        assertEquals(created.id(), retrieved.id());
        assertEquals(created.name(), retrieved.name());
        assertEquals(created.alias(), retrieved.alias());
        assertEquals(created.powers(), retrieved.powers());
        assertEquals(created.strengthLevel(), retrieved.strengthLevel());
    }

    // --- getById ---

    @Test
    void getById_returnsResponseForExistingId() {
        UUID existingId = saveDefaultHero();

        SuperheroResponse response = service.getById(existingId);

        assertEquals(existingId, response.id());
        assertEquals(VALID_NAME, response.name());
        assertEquals(VALID_ALIAS, response.alias());
        assertEquals(VALID_POWERS, response.powers());
        assertEquals(VALID_STRENGTH, response.strengthLevel());
    }

    @Test
    void getById_throwsSuperheroNotFoundExceptionForNonExistentId() {
        UUID nonExistentId = UUID.randomUUID();

        SuperheroNotFoundException exception = assertThrows(
            SuperheroNotFoundException.class,
            () -> service.getById(nonExistentId));

        assertTrue(exception.getMessage().contains(nonExistentId.toString()));
    }

    // --- getAll ---

    @Test
    void getAll_returnsEmptyListWhenRepositoryIsEmpty() {
        List<SuperheroResponse> responses = service.getAll();

        assertTrue(responses.isEmpty());
    }

    @Test
    void getAll_returnsAllSavedSuperheroes() {
        Superhero first = Superhero.create(VALID_NAME, VALID_ALIAS, VALID_POWERS, VALID_STRENGTH);
        Superhero second = Superhero.create("Iron Man", "Tony Stark", List.of("Powered armor"), 9);
        adapter.save(first);
        adapter.save(second);

        List<SuperheroResponse> responses = service.getAll();

        assertEquals(2, responses.size());
        assertTrue(responses.stream().anyMatch(r -> r.id().equals(first.getId())));
        assertTrue(responses.stream().anyMatch(r -> r.id().equals(second.getId())));
    }

    @Test
    void getAll_mapsAllFieldsToDto() {
        UUID id = saveDefaultHero();

        List<SuperheroResponse> responses = service.getAll();

        assertEquals(1, responses.size());
        SuperheroResponse response = responses.get(0);
        assertEquals(id, response.id());
        assertEquals(VALID_NAME, response.name());
        assertEquals(VALID_ALIAS, response.alias());
        assertEquals(VALID_POWERS, response.powers());
        assertEquals(VALID_STRENGTH, response.strengthLevel());
        assertNotNull(response.createdAt());
        assertNotNull(response.updatedAt());
    }

    // --- update ---

    @Test
    void update_updatesAllFieldsOfExistingSuperhero() {
        UUID existingId = saveDefaultHero();
        UpdateSuperheroRequest request = new UpdateSuperheroRequest(
            "Iron Man", "Tony Stark", List.of("Powered armor", "Genius intellect"), 9);

        SuperheroResponse response = service.update(existingId, request);

        assertEquals("Iron Man", response.name());
        assertEquals("Tony Stark", response.alias());
        assertEquals(List.of("Powered armor", "Genius intellect"), response.powers());
        assertEquals(9, response.strengthLevel());
        assertEquals(existingId, response.id());
    }

    @Test
    void update_refreshesUpdatedAtTimestamp() {
        UUID existingId = saveDefaultHero();
        UpdateSuperheroRequest request = new UpdateSuperheroRequest(
            VALID_NAME, "New Alias", VALID_POWERS, VALID_STRENGTH);

        SuperheroResponse response = service.update(existingId, request);

        assertNotNull(response.updatedAt());
    }

    @Test
    void update_throwsSuperheroNotFoundExceptionForNonExistentId() {
        UUID nonExistentId = UUID.randomUUID();
        UpdateSuperheroRequest request = new UpdateSuperheroRequest(
            VALID_NAME, VALID_ALIAS, VALID_POWERS, VALID_STRENGTH);

        SuperheroNotFoundException exception = assertThrows(
            SuperheroNotFoundException.class,
            () -> service.update(nonExistentId, request));

        assertTrue(exception.getMessage().contains(nonExistentId.toString()));
    }

    // --- delete ---

    @Test
    void delete_removesSuperheroFromRepository() {
        UUID existingId = saveDefaultHero();

        service.delete(existingId);

        assertFalse(adapter.existsById(existingId));
        assertTrue(adapter.findById(existingId).isEmpty());
    }

    @Test
    void delete_throwsSuperheroNotFoundExceptionForNonExistentId() {
        UUID nonExistentId = UUID.randomUUID();

        SuperheroNotFoundException exception = assertThrows(
            SuperheroNotFoundException.class,
            () -> service.delete(nonExistentId));

        assertTrue(exception.getMessage().contains(nonExistentId.toString()));
    }

    // --- helpers ---

    private UUID saveDefaultHero() {
        Superhero hero = Superhero.create(VALID_NAME, VALID_ALIAS, VALID_POWERS, VALID_STRENGTH);
        UUID id = hero.getId();
        adapter.save(hero);
        return id;
    }
}