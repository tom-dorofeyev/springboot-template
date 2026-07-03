package com.example.superheroes.superheroes.infrastructure.web;

import com.example.superheroes.superheroes.application.dto.CreateSuperheroRequest;
import com.example.superheroes.superheroes.application.dto.SuperheroResponse;
import com.example.superheroes.superheroes.application.dto.UpdateSuperheroRequest;
import com.example.superheroes.superheroes.application.service.SuperheroService;
import com.example.superheroes.superheroes.core.exception.SuperheroNotFoundException;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/superheroes")
public class SuperheroController {

    private final SuperheroService superheroService;

    public SuperheroController(SuperheroService superheroService) {
        this.superheroService = superheroService;
    }

    @PostMapping
    public ResponseEntity<SuperheroResponse> create(@Valid @RequestBody CreateSuperheroRequest request) {
        SuperheroResponse response = superheroService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuperheroResponse> getById(@PathVariable UUID id) {
        SuperheroResponse response = superheroService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<SuperheroResponse>> getAll() {
        List<SuperheroResponse> responses = superheroService.getAll();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuperheroResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateSuperheroRequest request) {
        SuperheroResponse response = superheroService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        superheroService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(SuperheroNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(SuperheroNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(
                fe -> errors.put(fe.getField(), fe.getDefaultMessage()));
        return ResponseEntity.badRequest().body(Map.of("errors", errors));
    }
}
