package com.example.superheroes.superheroes.infrastructure.persistence.sql;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

@Profile("sql")
public interface SqlSuperheroSpringRepository extends JpaRepository<SqlSuperheroEntity, UUID> {
}
