package com.mishchuk.autotrade.controller;

import com.mishchuk.autotrade.controller.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.UUID;
import service.SourceService;

@RestController
@RequestMapping("/sources")
@RequiredArgsConstructor
public class SourceController {

    private final SourceService sourceService;

    @PostMapping
    public ResponseEntity<SourceDetailDto> createSource(@Valid @RequestBody SourceCreateDto request) {
        return ResponseEntity.ok(sourceService.createSource(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SourceDetailDto> updateSource(@PathVariable UUID id, @RequestBody SourceUpdateDto request) {
        return ResponseEntity.ok(sourceService.updateSource(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SourceDetailDto> getSource(@PathVariable UUID id) {
        return ResponseEntity.ok(sourceService.getSource(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSource(@PathVariable UUID id) {
        sourceService.deleteSource(id);
        return ResponseEntity.noContent().build();
    }
}