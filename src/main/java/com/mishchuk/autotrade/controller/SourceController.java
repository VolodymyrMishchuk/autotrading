package com.mishchuk.autotrade.controller;

import com.mishchuk.autotrade.controller.dto.SourceCreateDto;
import com.mishchuk.autotrade.controller.dto.SourceDetailDto;
import com.mishchuk.autotrade.controller.dto.SourceUpdateDto;
import com.mishchuk.autotrade.service.model.Source;
import com.mishchuk.autotrade.service.source.SourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/sources")
@RequiredArgsConstructor
public class SourceController {

    private final SourceService sourceService;

    @PostMapping
    public ResponseEntity<SourceDetailDto> createSource(@Valid @RequestBody SourceCreateDto source) {
        return ResponseEntity.ok(sourceService.createSource(source));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SourceDetailDto> updateSource(@PathVariable UUID id, @RequestBody SourceUpdateDto source) {
        return ResponseEntity.ok(sourceService.updateSource(source));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Source> getSource(@PathVariable UUID id) {
        return ResponseEntity.ok(sourceService.getSource(String.valueOf(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSource(@PathVariable UUID id) {
        sourceService.deleteSource(String.valueOf(id));
        return ResponseEntity.noContent().build();
    }
}