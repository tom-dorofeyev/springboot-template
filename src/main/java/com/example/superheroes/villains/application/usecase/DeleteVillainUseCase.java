package com.example.superheroes.villains.application.usecase;

import com.example.superheroes.villains.core.exception.VillainNotFoundException;
import com.example.superheroes.villains.core.port.VillainRepository;
import java.util.UUID;

public class DeleteVillainUseCase {
    private final VillainRepository repository;

    public DeleteVillainUseCase(VillainRepository repository) {
        this.repository = repository;
    }

    public void execute(UUID id) {
        if (!repository.existsById(id)) {
            throw new VillainNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
