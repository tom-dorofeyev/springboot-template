package com.example.superheroes.superheroes.application.service;

import com.example.superheroes.superheroes.application.dto.CreateSuperheroRequest;
import com.example.superheroes.superheroes.application.dto.SuperheroResponse;
import com.example.superheroes.superheroes.application.dto.UpdateSuperheroRequest;
import java.util.List;
import java.util.UUID;

public interface SuperheroService {
    SuperheroResponse create(CreateSuperheroRequest request);
    SuperheroResponse getById(UUID id);
    List<SuperheroResponse> getAll();
    SuperheroResponse update(UUID id, UpdateSuperheroRequest request);
    void delete(UUID id);
}