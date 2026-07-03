package com.example.superheroes.superheroes.application.service;

import com.example.superheroes.superheroes.application.dto.CreateSuperheroRequest;
import com.example.superheroes.superheroes.application.dto.SuperheroResponse;
import com.example.superheroes.superheroes.application.dto.UpdateSuperheroRequest;
import com.example.superheroes.superheroes.core.exception.SuperheroNotFoundException;
import com.example.superheroes.superheroes.core.model.Superhero;
import com.example.superheroes.superheroes.core.port.SuperheroRepository;
import java.util.List;
import java.util.UUID;

public class SuperheroServiceImpl implements SuperheroService {

    private final SuperheroRepository repository;

    public SuperheroServiceImpl(SuperheroRepository repository) {
        this.repository = repository;
    }

    public SuperheroResponse create(CreateSuperheroRequest request) {
        Superhero superhero = Superhero.create(
            request.name(), request.alias(), request.powers(), request.strengthLevel());
        Superhero saved = repository.save(superhero);
        return SuperheroResponse.from(saved);
    }

    public SuperheroResponse getById(UUID id) {
        return repository.findById(id)
            .map(SuperheroResponse::from)
            .orElseThrow(() -> new SuperheroNotFoundException(id));
    }

    public List<SuperheroResponse> getAll() {
        return repository.findAll().stream()
            .map(SuperheroResponse::from)
            .toList();
    }

    public SuperheroResponse update(UUID id, UpdateSuperheroRequest request) {
        Superhero existing = repository.findById(id)
            .orElseThrow(() -> new SuperheroNotFoundException(id));
        existing.update(request.name(), request.alias(), request.powers(), request.strengthLevel());
        Superhero saved = repository.save(existing);
        return SuperheroResponse.from(saved);
    }

    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new SuperheroNotFoundException(id);
        }
        repository.deleteById(id);
    }
}