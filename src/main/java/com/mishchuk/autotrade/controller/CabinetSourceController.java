package com.mishchuk.autotrade.controller;

import com.mishchuk.autotrade.controller.dto.CabinetSourceDto;
import com.mishchuk.autotrade.controller.dto.CabinetSourceStatusUpdateDto;
import com.mishchuk.autotrade.service.cabinetsource.CabinetSourceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/cabinets/{cabinetId}/sources")
@RequiredArgsConstructor
public class CabinetSourceController {

    private final CabinetSourceService cabinetSourceService;

    @PostMapping("/{sourceId}")
    public ResponseEntity<CabinetSourceDto> addSource(
            @PathVariable UUID cabinetId,
            @PathVariable UUID sourceId
    ) {
        log.info("POST /api/cabinets/{}/sources/{} - add source", cabinetId, sourceId);
        CabinetSourceDto result = cabinetSourceService.addSourceToCabinet(cabinetId, sourceId);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{sourceId}")
    public ResponseEntity<CabinetSourceDto> updateStatus(
            @PathVariable UUID cabinetId,
            @PathVariable UUID sourceId,
            @Valid @RequestBody CabinetSourceStatusUpdateDto dto
    ) {
        log.info("PATCH /api/cabinets/{}/sources/{} - update status", cabinetId, sourceId);
        CabinetSourceDto updated = cabinetSourceService.updateSourceStatus(cabinetId, sourceId, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{sourceId}")
    public ResponseEntity<Void> removeSource(
            @PathVariable UUID cabinetId,
            @PathVariable UUID sourceId
    ) {
        log.info("DELETE /api/cabinets/{}/sources/{} - remove source", cabinetId, sourceId);
        cabinetSourceService.removeSourceFromCabinet(cabinetId, sourceId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CabinetSourceDto>> getSources(
            @PathVariable UUID cabinetId
    ) {
        log.info("GET /api/cabinets/{}/sources - fetch all", cabinetId);
        List<CabinetSourceDto> sources = cabinetSourceService.getSourcesByCabinet(cabinetId);
        return ResponseEntity.ok(sources);
    }
}