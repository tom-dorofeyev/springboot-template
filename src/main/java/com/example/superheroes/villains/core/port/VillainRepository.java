package com.example.superheroes.villains.core.port;

import com.example.superheroes.villains.core.model.Villain;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VillainRepository {
    Villain save(Villain villain);

    Optional<Villain> findById(UUID id);

    List<Villain> findAll();

    void deleteById(UUID id);

    boolean existsById(UUID id);
}
