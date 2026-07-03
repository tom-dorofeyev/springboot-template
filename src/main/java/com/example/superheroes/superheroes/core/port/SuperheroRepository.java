package com.example.superheroes.superheroes.core.port;

import com.example.superheroes.superheroes.core.model.Superhero;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SuperheroRepository {
    Superhero save(Superhero superhero);

    Optional<Superhero> findById(UUID id);

    List<Superhero> findAll();

    void deleteById(UUID id);

    boolean existsById(UUID id);
}
