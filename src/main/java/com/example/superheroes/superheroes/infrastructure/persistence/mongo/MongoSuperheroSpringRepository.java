package com.example.superheroes.superheroes.infrastructure.persistence.mongo;

import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.UUID;

@Profile("mongo")
public interface MongoSuperheroSpringRepository extends MongoRepository<MongoSuperheroDocument, UUID> {
}
