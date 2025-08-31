package com.mishchuk.autotrade.controller;

import com.mishchuk.autotrade.controller.dto.source.SourceCreateDto;
import com.mishchuk.autotrade.controller.dto.source.SourceDetailDto;
import com.mishchuk.autotrade.controller.dto.source.SourceUpdateDto;
import com.mishchuk.autotrade.service.source.SourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sources")
@RequiredArgsConstructor
public class SourceController {

    private final SourceService sourceService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<SourceDetailDto> createSource(@Valid @RequestBody SourceCreateDto source) {
        return ResponseEntity.ok(sourceService.createSource(source));
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<SourceDetailDto> updateSource(@PathVariable UUID id, @RequestBody SourceUpdateDto dto) {
        return ResponseEntity.ok(sourceService.updateSource(id, dto));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<SourceDetailDto> getSourceById(@PathVariable UUID id) {
        return ResponseEntity.ok(sourceService.getSourceById(id));
    }

    @GetMapping("/by-token/{token}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<SourceDetailDto> getSourceByToken(@PathVariable String token) {
        return ResponseEntity.ok(sourceService.getSourceByToken(token));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<SourceDetailDto>> getAllSources() {
        return ResponseEntity.ok(sourceService.getAllSources());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deleteSource(@PathVariable UUID id) {
        sourceService.deleteSource(id);
        return ResponseEntity.noContent().build();
    }
}