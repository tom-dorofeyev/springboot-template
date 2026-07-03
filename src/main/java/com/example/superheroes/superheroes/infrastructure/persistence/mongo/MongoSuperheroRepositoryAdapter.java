package com.example.superheroes.superheroes.infrastructure.persistence.mongo;

import com.example.superheroes.superheroes.core.model.Superhero;
import com.example.superheroes.superheroes.core.port.SuperheroRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Profile("mongo")
@Repository
public class MongoSuperheroRepositoryAdapter implements SuperheroRepository {

    private final MongoSuperheroSpringRepository mongoRepo;

    public MongoSuperheroRepositoryAdapter(MongoSuperheroSpringRepository mongoRepo) {
        this.mongoRepo = mongoRepo;
    }

    @Override
    public Superhero save(Superhero superhero) {
        MongoSuperheroDocument doc = MongoSuperheroDocument.from(superhero);
        MongoSuperheroDocument saved = mongoRepo.save(doc);
        return saved.toDomain();
    }

    @Override
    public Optional<Superhero> findById(UUID id) {
        return mongoRepo.findById(id).map(MongoSuperheroDocument::toDomain);
    }

    @Override
    public List<Superhero> findAll() {
        return mongoRepo.findAll().stream()
                .map(MongoSuperheroDocument::toDomain)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        mongoRepo.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return mongoRepo.existsById(id);
    }
}
