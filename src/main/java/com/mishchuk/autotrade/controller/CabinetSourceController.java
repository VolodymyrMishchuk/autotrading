package com.mishchuk.autotrade.controller;

import com.mishchuk.autotrade.controller.dto.cabinet.CabinetSourceDto;
import com.mishchuk.autotrade.controller.dto.cabinet.CabinetSourceStatusUpdateDto;
import com.mishchuk.autotrade.service.cabinetsource.CabinetSourceService;
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
@RequestMapping("/cabinets/{cabinetId}/sources")
@RequiredArgsConstructor
public class CabinetSourceController {

    private final CabinetSourceService cabinetSourceService;

    @PostMapping("/{sourceId}")
    @PreAuthorize("@authHelper.isCabinetOwner(#cabinetId, principal.id) or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<CabinetSourceDto> addSource(
            @PathVariable UUID cabinetId,
            @PathVariable UUID sourceId
    ) {
        log.info("POST /cabinets/{}/sources/{} - add source", cabinetId, sourceId);
        CabinetSourceDto result = cabinetSourceService.addSourceToCabinet(cabinetId, sourceId);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{sourceId}")
    @PreAuthorize("@authHelper.isCabinetOwner(#cabinetId, principal.id) or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<CabinetSourceDto> updateStatus(
            @PathVariable UUID cabinetId,
            @PathVariable UUID sourceId,
            @Valid @RequestBody CabinetSourceStatusUpdateDto dto
    ) {
        log.info("PATCH /cabinets/{}/sources/{} - update status", cabinetId, sourceId);
        CabinetSourceDto updated = cabinetSourceService.updateSourceStatus(cabinetId, sourceId, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{sourceId}")
    @PreAuthorize("@authHelper.isCabinetOwner(#cabinetId, principal.id) or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> removeSource(
            @PathVariable UUID cabinetId,
            @PathVariable UUID sourceId
    ) {
        log.info("DELETE /cabinets/{}/sources/{} - remove source", cabinetId, sourceId);
        cabinetSourceService.removeSourceFromCabinet(cabinetId, sourceId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("@authHelper.isCabinetOwner(#cabinetId, principal.id) or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<CabinetSourceDto>> getSources(
            @PathVariable UUID cabinetId
    ) {
        log.info("GET /cabinets/{}/sources - fetch all", cabinetId);
        List<CabinetSourceDto> sources = cabinetSourceService.getSourcesByCabinet(cabinetId);
        return ResponseEntity.ok(sources);
    }
}