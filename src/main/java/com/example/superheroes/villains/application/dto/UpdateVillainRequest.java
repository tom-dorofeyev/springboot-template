package com.example.superheroes.villains.application.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record UpdateVillainRequest(
    @NotBlank(message = "name must not be blank")
    String name,
    @NotBlank(message = "villainName must not be blank")
    String villainName,
    @NotEmpty(message = "powers must not be empty")
    List<@NotBlank(message = "each power must not be blank") String> powers,
    @Min(value = 1, message = "dangerLevel must be at least 1")
    @Max(value = 10, message = "dangerLevel must be at most 10")
    int dangerLevel
) {}
