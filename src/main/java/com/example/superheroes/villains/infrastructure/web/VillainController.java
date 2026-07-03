package com.example.superheroes.villains.infrastructure.web;

import com.example.superheroes.villains.application.dto.CreateVillainRequest;
import com.example.superheroes.villains.application.dto.UpdateVillainRequest;
import com.example.superheroes.villains.application.dto.VillainResponse;
import com.example.superheroes.villains.application.usecase.CreateVillainUseCase;
import com.example.superheroes.villains.application.usecase.DeleteVillainUseCase;
import com.example.superheroes.villains.application.usecase.GetAllVillainsUseCase;
import com.example.superheroes.villains.application.usecase.GetVillainUseCase;
import com.example.superheroes.villains.application.usecase.UpdateVillainUseCase;
import com.example.superheroes.villains.core.exception.VillainNotFoundException;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/villains")
public class VillainController {

    private final CreateVillainUseCase createVillainUseCase;
    private final GetVillainUseCase getVillainUseCase;
    private final GetAllVillainsUseCase getAllVillainsUseCase;
    private final UpdateVillainUseCase updateVillainUseCase;
    private final DeleteVillainUseCase deleteVillainUseCase;

    public VillainController(
            CreateVillainUseCase createVillainUseCase,
            GetVillainUseCase getVillainUseCase,
            GetAllVillainsUseCase getAllVillainsUseCase,
            UpdateVillainUseCase updateVillainUseCase,
            DeleteVillainUseCase deleteVillainUseCase) {
        this.createVillainUseCase = createVillainUseCase;
        this.getVillainUseCase = getVillainUseCase;
        this.getAllVillainsUseCase = getAllVillainsUseCase;
        this.updateVillainUseCase = updateVillainUseCase;
        this.deleteVillainUseCase = deleteVillainUseCase;
    }

    @PostMapping
    public ResponseEntity<VillainResponse> create(@Valid @RequestBody CreateVillainRequest request) {
        VillainResponse response = createVillainUseCase.execute(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VillainResponse> getById(@PathVariable UUID id) {
        VillainResponse response = getVillainUseCase.execute(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<VillainResponse>> getAll() {
        List<VillainResponse> responses = getAllVillainsUseCase.execute();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VillainResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateVillainRequest request) {
        VillainResponse response = updateVillainUseCase.execute(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteVillainUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(VillainNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(VillainNotFoundException ex) {
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
