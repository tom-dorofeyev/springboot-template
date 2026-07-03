package com.example.superheroes.villains.application.usecase;

import com.example.superheroes.villains.application.dto.UpdateVillainRequest;
import com.example.superheroes.villains.application.dto.VillainResponse;
import com.example.superheroes.villains.core.exception.VillainNotFoundException;
import com.example.superheroes.villains.core.model.Villain;
import com.example.superheroes.villains.core.port.VillainRepository;
import java.util.UUID;

public class UpdateVillainUseCase {
    private final VillainRepository repository;

    public UpdateVillainUseCase(VillainRepository repository) {
        this.repository = repository;
    }

    public VillainResponse execute(UUID id, UpdateVillainRequest request) {
        Villain existing = repository.findById(id)
            .orElseThrow(() -> new VillainNotFoundException(id));
        existing.update(request.name(), request.villainName(),
            request.powers(), request.dangerLevel());
        Villain saved = repository.save(existing);
        return VillainResponse.from(saved);
    }
}
