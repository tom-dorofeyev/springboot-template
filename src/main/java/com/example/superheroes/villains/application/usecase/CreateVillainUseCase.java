package com.example.superheroes.villains.application.usecase;

import com.example.superheroes.villains.application.dto.CreateVillainRequest;
import com.example.superheroes.villains.application.dto.VillainResponse;
import com.example.superheroes.villains.core.model.Villain;
import com.example.superheroes.villains.core.port.VillainRepository;

public class CreateVillainUseCase {
    private final VillainRepository repository;

    public CreateVillainUseCase(VillainRepository repository) {
        this.repository = repository;
    }

    public VillainResponse execute(CreateVillainRequest request) {
        Villain villain = Villain.create(request.name(), request.villainName(),
            request.powers(), request.dangerLevel());
        Villain saved = repository.save(villain);
        return VillainResponse.from(saved);
    }
}
