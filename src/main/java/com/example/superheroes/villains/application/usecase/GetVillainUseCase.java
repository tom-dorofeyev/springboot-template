package com.example.superheroes.villains.application.usecase;

import com.example.superheroes.villains.application.dto.VillainResponse;
import com.example.superheroes.villains.core.exception.VillainNotFoundException;
import com.example.superheroes.villains.core.port.VillainRepository;
import java.util.UUID;

public class GetVillainUseCase {
    private final VillainRepository repository;

    public GetVillainUseCase(VillainRepository repository) {
        this.repository = repository;
    }

    public VillainResponse execute(UUID id) {
        return repository.findById(id)
            .map(VillainResponse::from)
            .orElseThrow(() -> new VillainNotFoundException(id));
    }
}
