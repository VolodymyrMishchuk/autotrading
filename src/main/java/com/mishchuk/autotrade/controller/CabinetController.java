package com.mishchuk.autotrade.controller;

import com.mishchuk.autotrade.controller.dto.CabinetCreateDto;
import com.mishchuk.autotrade.controller.dto.CabinetDetailDto;
import com.mishchuk.autotrade.controller.dto.CabinetUpdateDto;
import com.mishchuk.autotrade.service.cabinet.CabinetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/cabinets")
@RequiredArgsConstructor
public class CabinetController {

    private final CabinetService cabinetService;

    @PostMapping
    public ResponseEntity<CabinetDetailDto> createCabinet(
            @RequestParam UUID userId,
            @Valid @RequestBody CabinetCreateDto dto
    ) {
        log.info("POST /api/cabinets - create cabinet for user {}", userId);
        CabinetDetailDto created = cabinetService.createCabinet(userId, dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CabinetDetailDto> updateCabinet(
            @PathVariable UUID id,
            @Valid @RequestBody CabinetUpdateDto dto
    ) {
        log.info("PUT /api/cabinets/{} - update cabinet", id);
        CabinetDetailDto updated = cabinetService.updateCabinet(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCabinet(@PathVariable UUID id) {
        log.info("DELETE /api/cabinets/{} - delete cabinet", id);
        cabinetService.deleteCabinet(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CabinetDetailDto> getCabinetById(@PathVariable UUID id) {
        log.info("GET /api/cabinets/{} - get cabinet", id);
        return ResponseEntity.ok(cabinetService.getCabinetById(id));
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<CabinetDetailDto>> getCabinetsByUserId(@PathVariable UUID userId) {
        log.info("GET /api/cabinets/by-user/{} - get all cabinets for user", userId);
        return ResponseEntity.ok(cabinetService.getCabinetsByUserId(userId));
    }

    @GetMapping("/by-account/{accountId}")
    public ResponseEntity<List<CabinetDetailDto>> getCabinetsByAccountId(@PathVariable UUID accountId) {
        log.info("GET /api/cabinets/by-account/{} - get all cabinets for account", accountId);
        return ResponseEntity.ok(cabinetService.getCabinetsByAccountId(accountId));
    }
}