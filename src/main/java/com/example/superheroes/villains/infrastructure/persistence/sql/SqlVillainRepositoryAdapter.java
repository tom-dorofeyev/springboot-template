package com.example.superheroes.villains.infrastructure.persistence.sql;

import com.example.superheroes.villains.core.model.Villain;
import com.example.superheroes.villains.core.port.VillainRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Profile("sql")
@Repository
public class SqlVillainRepositoryAdapter implements VillainRepository {

    private final SqlVillainSpringRepository jpaRepo;

    public SqlVillainRepositoryAdapter(SqlVillainSpringRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public Villain save(Villain villain) {
        SqlVillainEntity entity = SqlVillainEntity.from(villain);
        SqlVillainEntity saved = jpaRepo.save(entity);
        return saved.toDomain();
    }

    @Override
    public Optional<Villain> findById(UUID id) {
        return jpaRepo.findById(id).map(SqlVillainEntity::toDomain);
    }

    @Override
    public List<Villain> findAll() {
        return jpaRepo.findAll().stream()
                .map(SqlVillainEntity::toDomain)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepo.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaRepo.existsById(id);
    }
}