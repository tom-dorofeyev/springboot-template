package com.example.superheroes.superheroes.infrastructure.persistence.sql;

import com.example.superheroes.superheroes.core.model.Superhero;
import com.example.superheroes.superheroes.core.port.SuperheroRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Profile("sql")
@Repository
public class SqlSuperheroRepositoryAdapter implements SuperheroRepository {

    private final SqlSuperheroSpringRepository jpaRepo;

    public SqlSuperheroRepositoryAdapter(SqlSuperheroSpringRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public Superhero save(Superhero superhero) {
        SqlSuperheroEntity entity = SqlSuperheroEntity.from(superhero);
        SqlSuperheroEntity saved = jpaRepo.save(entity);
        return saved.toDomain();
    }

    @Override
    public Optional<Superhero> findById(UUID id) {
        return jpaRepo.findById(id).map(SqlSuperheroEntity::toDomain);
    }

    @Override
    public List<Superhero> findAll() {
        return jpaRepo.findAll().stream()
                .map(SqlSuperheroEntity::toDomain)
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
