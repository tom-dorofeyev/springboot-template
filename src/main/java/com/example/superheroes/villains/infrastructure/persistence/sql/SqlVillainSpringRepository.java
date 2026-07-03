package com.example.superheroes.villains.infrastructure.persistence.sql;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

@Profile("sql")
public interface SqlVillainSpringRepository extends JpaRepository<SqlVillainEntity, UUID> {
}