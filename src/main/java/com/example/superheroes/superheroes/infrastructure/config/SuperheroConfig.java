package com.example.superheroes.superheroes.infrastructure.config;

import com.example.superheroes.superheroes.application.service.SuperheroService;
import com.example.superheroes.superheroes.application.service.SuperheroServiceImpl;
import com.example.superheroes.superheroes.core.port.SuperheroRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SuperheroConfig {

    @Bean
    public SuperheroService superheroService(SuperheroRepository repository) {
        return new SuperheroServiceImpl(repository);
    }
}