package com.example.superheroes.villains.infrastructure.persistence.mongo;

import com.example.superheroes.villains.core.model.Villain;
import com.example.superheroes.villains.core.port.VillainRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Profile("mongo")
@Repository
public class MongoVillainRepositoryAdapter implements VillainRepository {

    private final MongoVillainSpringRepository mongoRepo;

    public MongoVillainRepositoryAdapter(MongoVillainSpringRepository mongoRepo) {
        this.mongoRepo = mongoRepo;
    }

    @Override
    public Villain save(Villain villain) {
        MongoVillainDocument doc = MongoVillainDocument.from(villain);
        MongoVillainDocument saved = mongoRepo.save(doc);
        return saved.toDomain();
    }

    @Override
    public Optional<Villain> findById(UUID id) {
        return mongoRepo.findById(id).map(MongoVillainDocument::toDomain);
    }

    @Override
    public List<Villain> findAll() {
        return mongoRepo.findAll().stream()
                .map(MongoVillainDocument::toDomain)
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