package com.example.superheroes.villains.infrastructure.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.superheroes.villains.application.dto.CreateVillainRequest;
import com.example.superheroes.villains.application.dto.UpdateVillainRequest;
import com.example.superheroes.villains.application.dto.VillainResponse;
import com.example.superheroes.villains.application.usecase.CreateVillainUseCase;
import com.example.superheroes.villains.application.usecase.DeleteVillainUseCase;
import com.example.superheroes.villains.application.usecase.GetAllVillainsUseCase;
import com.example.superheroes.villains.application.usecase.GetVillainUseCase;
import com.example.superheroes.villains.application.usecase.UpdateVillainUseCase;
import com.example.superheroes.villains.core.exception.VillainNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class VillainControllerTest {

    private static final UUID SAMPLE_ID = UUID.fromString("00000000-0000-0000-0000-000000000002");
    private static final String SAMPLE_NAME = "Loki";
    private static final String SAMPLE_VILLAIN_NAME = "God of Mischief";
    private static final List<String> SAMPLE_POWERS = List.of("Illusions", "Shapeshifting");
    private static final int SAMPLE_DANGER_LEVEL = 8;
    private static final Instant SAMPLE_TIMESTAMP = Instant.parse("2025-01-01T00:00:00Z");

    private static final VillainResponse SAMPLE_RESPONSE = new VillainResponse(
            SAMPLE_ID, SAMPLE_NAME, SAMPLE_VILLAIN_NAME, SAMPLE_POWERS, SAMPLE_DANGER_LEVEL,
            SAMPLE_TIMESTAMP, SAMPLE_TIMESTAMP);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateVillainUseCase createVillainUseCase;

    @MockitoBean
    private GetVillainUseCase getVillainUseCase;

    @MockitoBean
    private GetAllVillainsUseCase getAllVillainsUseCase;

    @MockitoBean
    private UpdateVillainUseCase updateVillainUseCase;

    @MockitoBean
    private DeleteVillainUseCase deleteVillainUseCase;

    @Test
    void createReturns201WithLocationHeader() throws Exception {
        CreateVillainRequest request = new CreateVillainRequest(
                SAMPLE_NAME, SAMPLE_VILLAIN_NAME, SAMPLE_POWERS, SAMPLE_DANGER_LEVEL);
        when(createVillainUseCase.execute(any(CreateVillainRequest.class)))
                .thenReturn(SAMPLE_RESPONSE);

        mockMvc.perform(post("/api/v1/villains")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location",
                        "http://localhost/api/v1/villains/" + SAMPLE_ID))
                .andExpect(jsonPath("$.id").value(SAMPLE_ID.toString()))
                .andExpect(jsonPath("$.name").value(SAMPLE_NAME));
    }

    @Test
    void getByIdReturns200WithBody() throws Exception {
        when(getVillainUseCase.execute(SAMPLE_ID)).thenReturn(SAMPLE_RESPONSE);

        mockMvc.perform(get("/api/v1/villains/" + SAMPLE_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(SAMPLE_ID.toString()))
                .andExpect(jsonPath("$.name").value(SAMPLE_NAME))
                .andExpect(jsonPath("$.villainName").value(SAMPLE_VILLAIN_NAME));
    }

    @Test
    void getByIdNotFoundReturns404() throws Exception {
        UUID nonExistentId = UUID.randomUUID();
        when(getVillainUseCase.execute(nonExistentId))
                .thenThrow(new VillainNotFoundException(nonExistentId));

        mockMvc.perform(get("/api/v1/villains/" + nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(
                        "Villain not found with id: " + nonExistentId));
    }

    @Test
    void getAllReturns200WithList() throws Exception {
        VillainResponse second = new VillainResponse(
                UUID.randomUUID(), "Thanos", "The Mad Titan",
                List.of("Infinity Gauntlet"), 10, SAMPLE_TIMESTAMP, SAMPLE_TIMESTAMP);
        when(getAllVillainsUseCase.execute()).thenReturn(List.of(SAMPLE_RESPONSE, second));

        mockMvc.perform(get("/api/v1/villains"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(SAMPLE_ID.toString()))
                .andExpect(jsonPath("$[1].name").value("Thanos"));
    }

    @Test
    void updateReturns200WithUpdatedBody() throws Exception {
        UpdateVillainRequest request = new UpdateVillainRequest(
                "Thanos", "The Mad Titan", List.of("Infinity Gauntlet"), 10);
        UUID idToUpdate = UUID.randomUUID();
        VillainResponse updated = new VillainResponse(
                idToUpdate, "Thanos", "The Mad Titan",
                List.of("Infinity Gauntlet"), 10, SAMPLE_TIMESTAMP, SAMPLE_TIMESTAMP);
        when(updateVillainUseCase.execute(any(UUID.class), any(UpdateVillainRequest.class)))
                .thenReturn(updated);

        mockMvc.perform(put("/api/v1/villains/" + idToUpdate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(idToUpdate.toString()))
                .andExpect(jsonPath("$.name").value("Thanos"))
                .andExpect(jsonPath("$.villainName").value("The Mad Titan"))
                .andExpect(jsonPath("$.dangerLevel").value(10));
    }

    @Test
    void updateNotFoundReturns404() throws Exception {
        UUID nonExistentId = UUID.randomUUID();
        UpdateVillainRequest request = new UpdateVillainRequest(
                SAMPLE_NAME, SAMPLE_VILLAIN_NAME, SAMPLE_POWERS, SAMPLE_DANGER_LEVEL);
        when(updateVillainUseCase.execute(any(UUID.class), any(UpdateVillainRequest.class)))
                .thenThrow(new VillainNotFoundException(nonExistentId));

        mockMvc.perform(put("/api/v1/villains/" + nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(
                        "Villain not found with id: " + nonExistentId));
    }

    @Test
    void deleteReturns204() throws Exception {
        mockMvc.perform(delete("/api/v1/villains/" + SAMPLE_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteNotFoundReturns404() throws Exception {
        UUID nonExistentId = UUID.randomUUID();
        doThrow(new VillainNotFoundException(nonExistentId))
                .when(deleteVillainUseCase).execute(nonExistentId);

        mockMvc.perform(delete("/api/v1/villains/" + nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(
                        "Villain not found with id: " + nonExistentId));
    }

    @Test
    void createWithInvalidBodyReturns400() throws Exception {
        CreateVillainRequest invalidRequest = new CreateVillainRequest(
                "", "", List.of(), 0);

        mockMvc.perform(post("/api/v1/villains")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isMap())
                .andExpect(jsonPath("$.errors.name").value("name must not be blank"));
    }
}
