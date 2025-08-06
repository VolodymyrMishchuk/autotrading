package com.mishchuk.autotrade.controller;

import com.mishchuk.autotrade.controller.dto.CabinetCreateDto;
import com.mishchuk.autotrade.controller.dto.CabinetDetailDto;
import com.mishchuk.autotrade.controller.dto.CabinetUpdateDto;
import com.mishchuk.autotrade.service.cabinet.CabinetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/cabinets")
@RequiredArgsConstructor
public class CabinetController {

    private final CabinetService cabinetService;

    @PostMapping
    @PreAuthorize("#dto.userId.toString() == principal.username or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<CabinetDetailDto> createCabinet(
            @Valid @RequestBody CabinetCreateDto dto
    ) {
        log.info("POST /cabinets - create cabinet for user {}", dto.getUserId());
        CabinetDetailDto created = cabinetService.createCabinet(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{cabinetId}")
    @PreAuthorize("@authHelper.isCabinetOwner(#cabinetId, principal.username) or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<CabinetDetailDto> updateCabinet(
            @PathVariable UUID cabinetId,
            @Valid @RequestBody CabinetUpdateDto dto
    ) {
        log.info("PUT /cabinets/{} - update cabinet", cabinetId);
        CabinetDetailDto updated = cabinetService.updateCabinet(cabinetId, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{cabinetId}")
    @PreAuthorize("@authHelper.isCabinetOwner(#cabinetId, principal.username) or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deleteCabinet(@PathVariable UUID cabinetId) {
        log.info("DELETE /api/cabinets/{} - delete cabinet", cabinetId);
        cabinetService.deleteCabinet(cabinetId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{cabinetId}")
    @PreAuthorize("@authHelper.isCabinetOwner(#cabinetId, principal.username) or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<CabinetDetailDto> getCabinetById(@PathVariable UUID cabinetId) {
        log.info("GET /api/cabinets/{} - get cabinet", cabinetId);
        return ResponseEntity.ok(cabinetService.getCabinetById(cabinetId));
    }

    @GetMapping("/by-user/{userId}")
    @PreAuthorize("#userId.toString() == principal.username or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<CabinetDetailDto>> getCabinetsByUserId(@PathVariable UUID userId) {
        log.info("GET /api/cabinets/by-user/{} - get all cabinets for user", userId);
        return ResponseEntity.ok(cabinetService.getCabinetsByUserId(userId));
    }

    @GetMapping("/by-account/{accountId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<CabinetDetailDto>> getCabinetsByAccountId(@PathVariable UUID accountId) {
        log.info("GET /api/cabinets/by-account/{} - get all cabinets for account", accountId);
        return ResponseEntity.ok(cabinetService.getCabinetsByAccountId(accountId));
    }
}