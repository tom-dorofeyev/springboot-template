package com.example.superheroes.villains.application.usecase;

import com.example.superheroes.villains.application.dto.VillainResponse;
import com.example.superheroes.villains.core.port.VillainRepository;
import java.util.List;

public class GetAllVillainsUseCase {
    private final VillainRepository repository;

    public GetAllVillainsUseCase(VillainRepository repository) {
        this.repository = repository;
    }

    public List<VillainResponse> execute() {
        return repository.findAll().stream()
            .map(VillainResponse::from)
            .toList();
    }
}
