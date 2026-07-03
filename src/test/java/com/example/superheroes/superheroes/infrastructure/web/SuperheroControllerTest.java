package com.example.superheroes.superheroes.infrastructure.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.superheroes.superheroes.application.dto.CreateSuperheroRequest;
import com.example.superheroes.superheroes.application.dto.SuperheroResponse;
import com.example.superheroes.superheroes.application.dto.UpdateSuperheroRequest;
import com.example.superheroes.superheroes.application.service.SuperheroService;
import com.example.superheroes.superheroes.core.exception.SuperheroNotFoundException;
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
class SuperheroControllerTest {

    private static final UUID SAMPLE_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final String SAMPLE_NAME = "Spider-Man";
    private static final String SAMPLE_ALIAS = "Peter Parker";
    private static final List<String> SAMPLE_POWERS = List.of("Web-slinging", "Spider-sense");
    private static final int SAMPLE_STRENGTH = 7;
    private static final Instant SAMPLE_TIMESTAMP = Instant.parse("2025-01-01T00:00:00Z");

    private static final SuperheroResponse SAMPLE_RESPONSE = new SuperheroResponse(
            SAMPLE_ID, SAMPLE_NAME, SAMPLE_ALIAS, SAMPLE_POWERS, SAMPLE_STRENGTH,
            SAMPLE_TIMESTAMP, SAMPLE_TIMESTAMP);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SuperheroService superheroService;

    @Test
    void createReturns201WithLocationHeader() throws Exception {
        CreateSuperheroRequest request = new CreateSuperheroRequest(
                SAMPLE_NAME, SAMPLE_ALIAS, SAMPLE_POWERS, SAMPLE_STRENGTH);
        when(superheroService.create(any(CreateSuperheroRequest.class)))
                .thenReturn(SAMPLE_RESPONSE);

        mockMvc.perform(post("/api/v1/superheroes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location",
                        "http://localhost/api/v1/superheroes/" + SAMPLE_ID))
                .andExpect(jsonPath("$.id").value(SAMPLE_ID.toString()))
                .andExpect(jsonPath("$.name").value(SAMPLE_NAME));
    }

    @Test
    void getByIdReturns200WithBody() throws Exception {
        when(superheroService.getById(SAMPLE_ID)).thenReturn(SAMPLE_RESPONSE);

        mockMvc.perform(get("/api/v1/superheroes/" + SAMPLE_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(SAMPLE_ID.toString()))
                .andExpect(jsonPath("$.name").value(SAMPLE_NAME))
                .andExpect(jsonPath("$.alias").value(SAMPLE_ALIAS));
    }

    @Test
    void getByIdNotFoundReturns404() throws Exception {
        UUID nonExistentId = UUID.randomUUID();
        when(superheroService.getById(nonExistentId))
                .thenThrow(new SuperheroNotFoundException(nonExistentId));

        mockMvc.perform(get("/api/v1/superheroes/" + nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(
                        "Superhero not found with id: " + nonExistentId));
    }

    @Test
    void getAllReturns200WithList() throws Exception {
        SuperheroResponse second = new SuperheroResponse(
                UUID.randomUUID(), "Iron Man", "Tony Stark",
                List.of("Powered armor"), 9, SAMPLE_TIMESTAMP, SAMPLE_TIMESTAMP);
        when(superheroService.getAll()).thenReturn(List.of(SAMPLE_RESPONSE, second));

        mockMvc.perform(get("/api/v1/superheroes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(SAMPLE_ID.toString()))
                .andExpect(jsonPath("$[1].name").value("Iron Man"));
    }

    @Test
    void updateReturns200WithUpdatedBody() throws Exception {
        UpdateSuperheroRequest request = new UpdateSuperheroRequest(
                "Iron Man", "Tony Stark", List.of("Powered armor"), 9);
        UUID idToUpdate = UUID.randomUUID();
        SuperheroResponse updated = new SuperheroResponse(
                idToUpdate, "Iron Man", "Tony Stark",
                List.of("Powered armor"), 9, SAMPLE_TIMESTAMP, SAMPLE_TIMESTAMP);
        when(superheroService.update(any(UUID.class), any(UpdateSuperheroRequest.class)))
                .thenReturn(updated);

        mockMvc.perform(put("/api/v1/superheroes/" + idToUpdate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(idToUpdate.toString()))
                .andExpect(jsonPath("$.name").value("Iron Man"))
                .andExpect(jsonPath("$.alias").value("Tony Stark"))
                .andExpect(jsonPath("$.strengthLevel").value(9));
    }

    @Test
    void updateNotFoundReturns404() throws Exception {
        UUID nonExistentId = UUID.randomUUID();
        UpdateSuperheroRequest request = new UpdateSuperheroRequest(
                SAMPLE_NAME, SAMPLE_ALIAS, SAMPLE_POWERS, SAMPLE_STRENGTH);
        when(superheroService.update(any(UUID.class), any(UpdateSuperheroRequest.class)))
                .thenThrow(new SuperheroNotFoundException(nonExistentId));

        mockMvc.perform(put("/api/v1/superheroes/" + nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(
                        "Superhero not found with id: " + nonExistentId));
    }

    @Test
    void deleteReturns204() throws Exception {
        mockMvc.perform(delete("/api/v1/superheroes/" + SAMPLE_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteNotFoundReturns404() throws Exception {
        UUID nonExistentId = UUID.randomUUID();
        doThrow(new SuperheroNotFoundException(nonExistentId))
                .when(superheroService).delete(nonExistentId);

        mockMvc.perform(delete("/api/v1/superheroes/" + nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(
                        "Superhero not found with id: " + nonExistentId));
    }

    @Test
    void createWithInvalidBodyReturns400() throws Exception {
        CreateSuperheroRequest invalidRequest = new CreateSuperheroRequest(
                "", "", List.of(), 0);

        mockMvc.perform(post("/api/v1/superheroes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isMap())
                .andExpect(jsonPath("$.errors.name").value("name must not be blank"));
    }
}
