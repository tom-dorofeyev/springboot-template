package com.example.superheroes.villains.infrastructure.persistence.inmemory;

import com.example.superheroes.villains.core.model.Villain;
import com.example.superheroes.villains.core.port.VillainRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Profile("inmemory")
@Repository
public class InMemoryVillainRepositoryAdapter implements VillainRepository {
    private final ConcurrentHashMap<UUID, Villain> store = new ConcurrentHashMap<>();

    @Override
    public Villain save(Villain villain) {
        store.put(villain.getId(), villain);
        return villain;
    }

    @Override
    public Optional<Villain> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Villain> findAll() {
        return List.copyOf(store.values());
    }

    @Override
    public void deleteById(UUID id) {
        store.remove(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return store.containsKey(id);
    }
}
