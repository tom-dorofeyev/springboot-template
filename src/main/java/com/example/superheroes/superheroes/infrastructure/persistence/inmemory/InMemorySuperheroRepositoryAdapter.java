package com.example.superheroes.superheroes.infrastructure.persistence.inmemory;

import com.example.superheroes.superheroes.core.model.Superhero;
import com.example.superheroes.superheroes.core.port.SuperheroRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Profile("inmemory")
@Repository
public class InMemorySuperheroRepositoryAdapter implements SuperheroRepository {
    private final ConcurrentHashMap<UUID, Superhero> store = new ConcurrentHashMap<>();

    @Override
    public Superhero save(Superhero superhero) {
        store.put(superhero.getId(), superhero);
        return superhero;
    }

    @Override
    public Optional<Superhero> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Superhero> findAll() {
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
