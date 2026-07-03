package com.example.superheroes.villains.infrastructure.config;

import com.example.superheroes.villains.application.usecase.CreateVillainUseCase;
import com.example.superheroes.villains.application.usecase.DeleteVillainUseCase;
import com.example.superheroes.villains.application.usecase.GetAllVillainsUseCase;
import com.example.superheroes.villains.application.usecase.GetVillainUseCase;
import com.example.superheroes.villains.application.usecase.UpdateVillainUseCase;
import com.example.superheroes.villains.core.port.VillainRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VillainConfig {

    @Bean
    public CreateVillainUseCase createVillainUseCase(VillainRepository repository) {
        return new CreateVillainUseCase(repository);
    }

    @Bean
    public GetVillainUseCase getVillainUseCase(VillainRepository repository) {
        return new GetVillainUseCase(repository);
    }

    @Bean
    public GetAllVillainsUseCase getAllVillainsUseCase(VillainRepository repository) {
        return new GetAllVillainsUseCase(repository);
    }

    @Bean
    public UpdateVillainUseCase updateVillainUseCase(VillainRepository repository) {
        return new UpdateVillainUseCase(repository);
    }

    @Bean
    public DeleteVillainUseCase deleteVillainUseCase(VillainRepository repository) {
        return new DeleteVillainUseCase(repository);
    }
}
