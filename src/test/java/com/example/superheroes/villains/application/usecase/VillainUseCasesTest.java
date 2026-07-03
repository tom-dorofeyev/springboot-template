package com.example.superheroes.villains.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.superheroes.villains.application.dto.CreateVillainRequest;
import com.example.superheroes.villains.application.dto.UpdateVillainRequest;
import com.example.superheroes.villains.application.dto.VillainResponse;
import com.example.superheroes.villains.core.exception.VillainNotFoundException;
import com.example.superheroes.villains.core.model.Villain;
import com.example.superheroes.villains.infrastructure.persistence.inmemory.InMemoryVillainRepositoryAdapter;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VillainUseCasesTest {

    private static final String VALID_NAME = "Loki";
    private static final String VALID_VILLAIN_NAME = "God of Mischief";
    private static final List<String> VALID_POWERS = List.of("Illusions", "Shapeshifting");
    private static final int VALID_DANGER_LEVEL = 8;

    private InMemoryVillainRepositoryAdapter repository;
    private CreateVillainUseCase createUseCase;
    private GetVillainUseCase getUseCase;
    private GetAllVillainsUseCase getAllUseCase;
    private UpdateVillainUseCase updateUseCase;
    private DeleteVillainUseCase deleteUseCase;

    @BeforeEach
    void setUp() {
        repository = new InMemoryVillainRepositoryAdapter();
        createUseCase = new CreateVillainUseCase(repository);
        getUseCase = new GetVillainUseCase(repository);
        getAllUseCase = new GetAllVillainsUseCase(repository);
        updateUseCase = new UpdateVillainUseCase(repository);
        deleteUseCase = new DeleteVillainUseCase(repository);
    }

    private UUID saveDefaultVillain() {
        Villain villain = Villain.create(VALID_NAME, VALID_VILLAIN_NAME, VALID_POWERS, VALID_DANGER_LEVEL);
        UUID id = villain.getId();
        repository.save(villain);
        return id;
    }

    private CreateVillainRequest defaultRequest() {
        return new CreateVillainRequest(VALID_NAME, VALID_VILLAIN_NAME, VALID_POWERS, VALID_DANGER_LEVEL);
    }

    // --- create ---

    @Test
    void createReturnsResponseWithAllFieldsMatchingRequest() {
        VillainResponse response = createUseCase.execute(defaultRequest());

        assertNotNull(response.id());
        assertEquals(VALID_NAME, response.name());
        assertEquals(VALID_VILLAIN_NAME, response.villainName());
        assertEquals(VALID_POWERS, response.powers());
        assertEquals(VALID_DANGER_LEVEL, response.dangerLevel());
        assertNotNull(response.createdAt());
        assertNotNull(response.updatedAt());
    }

    @Test
    void createGeneratesUniqueIdForEachCreate() {
        VillainResponse first = createUseCase.execute(defaultRequest());
        VillainResponse second = createUseCase.execute(defaultRequest());

        assertNotEquals(first.id(), second.id());
    }

    @Test
    void createRoundTripsThroughGetById() {
        VillainResponse created = createUseCase.execute(defaultRequest());
        VillainResponse retrieved = getUseCase.execute(created.id());

        assertEquals(created.id(), retrieved.id());
        assertEquals(created.name(), retrieved.name());
        assertEquals(created.villainName(), retrieved.villainName());
        assertEquals(created.powers(), retrieved.powers());
        assertEquals(created.dangerLevel(), retrieved.dangerLevel());
    }

    // --- getById ---

    @Test
    void getByIdReturnsResponseForExistingId() {
        UUID existingId = saveDefaultVillain();

        VillainResponse response = getUseCase.execute(existingId);

        assertEquals(existingId, response.id());
        assertEquals(VALID_NAME, response.name());
        assertEquals(VALID_VILLAIN_NAME, response.villainName());
        assertEquals(VALID_POWERS, response.powers());
        assertEquals(VALID_DANGER_LEVEL, response.dangerLevel());
    }

    @Test
    void getByIdThrowsVillainNotFoundExceptionForNonExistentId() {
        UUID nonExistentId = UUID.randomUUID();

        VillainNotFoundException exception = assertThrows(
            VillainNotFoundException.class,
            () -> getUseCase.execute(nonExistentId));

        assertTrue(exception.getMessage().contains(nonExistentId.toString()));
    }

    // --- getAll ---

    @Test
    void getAllReturnsEmptyListWhenRepositoryIsEmpty() {
        List<VillainResponse> responses = getAllUseCase.execute();

        assertTrue(responses.isEmpty());
    }

    @Test
    void getAllReturnsAllSavedVillains() {
        Villain first = Villain.create(VALID_NAME, VALID_VILLAIN_NAME, VALID_POWERS, VALID_DANGER_LEVEL);
        Villain second = Villain.create("Thanos", "The Mad Titan", List.of("Infinity Gauntlet"), 10);
        repository.save(first);
        repository.save(second);

        List<VillainResponse> responses = getAllUseCase.execute();

        assertEquals(2, responses.size());
        assertTrue(responses.stream().anyMatch(r -> r.id().equals(first.getId())));
        assertTrue(responses.stream().anyMatch(r -> r.id().equals(second.getId())));
    }

    @Test
    void getAllMapsAllFieldsToDto() {
        UUID id = saveDefaultVillain();

        List<VillainResponse> responses = getAllUseCase.execute();

        assertEquals(1, responses.size());
        VillainResponse response = responses.get(0);
        assertEquals(id, response.id());
        assertEquals(VALID_NAME, response.name());
        assertEquals(VALID_VILLAIN_NAME, response.villainName());
        assertEquals(VALID_POWERS, response.powers());
        assertEquals(VALID_DANGER_LEVEL, response.dangerLevel());
        assertNotNull(response.createdAt());
        assertNotNull(response.updatedAt());
    }

    // --- update ---

    @Test
    void updateUpdatesAllFieldsOfExistingVillain() {
        UUID existingId = saveDefaultVillain();
        UpdateVillainRequest request = new UpdateVillainRequest(
            "Thanos", "The Mad Titan", List.of("Infinity Gauntlet", "Super strength"), 10);

        VillainResponse response = updateUseCase.execute(existingId, request);

        assertEquals("Thanos", response.name());
        assertEquals("The Mad Titan", response.villainName());
        assertEquals(List.of("Infinity Gauntlet", "Super strength"), response.powers());
        assertEquals(10, response.dangerLevel());
        assertEquals(existingId, response.id());
    }

    @Test
    void updateRefreshesUpdatedAtTimestamp() {
        UUID existingId = saveDefaultVillain();
        UpdateVillainRequest request = new UpdateVillainRequest(
            VALID_NAME, "New Villain Name", VALID_POWERS, VALID_DANGER_LEVEL);

        VillainResponse response = updateUseCase.execute(existingId, request);

        assertNotNull(response.updatedAt());
    }

    @Test
    void updateThrowsVillainNotFoundExceptionForNonExistentId() {
        UUID nonExistentId = UUID.randomUUID();
        UpdateVillainRequest request = new UpdateVillainRequest(
            VALID_NAME, VALID_VILLAIN_NAME, VALID_POWERS, VALID_DANGER_LEVEL);

        VillainNotFoundException exception = assertThrows(
            VillainNotFoundException.class,
            () -> updateUseCase.execute(nonExistentId, request));

        assertTrue(exception.getMessage().contains(nonExistentId.toString()));
    }

    // --- delete ---

    @Test
    void deleteRemovesVillainFromRepository() {
        UUID existingId = saveDefaultVillain();

        deleteUseCase.execute(existingId);

        assertFalse(repository.existsById(existingId));
        assertTrue(repository.findById(existingId).isEmpty());
    }

    @Test
    void deleteThrowsVillainNotFoundExceptionForNonExistentId() {
        UUID nonExistentId = UUID.randomUUID();

        VillainNotFoundException exception = assertThrows(
            VillainNotFoundException.class,
            () -> deleteUseCase.execute(nonExistentId));

        assertTrue(exception.getMessage().contains(nonExistentId.toString()));
    }
}
