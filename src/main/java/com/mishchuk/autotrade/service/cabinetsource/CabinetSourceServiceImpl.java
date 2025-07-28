package com.mishchuk.autotrade.service.cabinetsource;

import com.mishchuk.autotrade.controller.dto.CabinetSourceDto;
import com.mishchuk.autotrade.controller.dto.CabinetSourceStatusUpdateDto;
import com.mishchuk.autotrade.enums.Status;
import com.mishchuk.autotrade.exception.CabinetNotFoundException;
import com.mishchuk.autotrade.exception.CabinetSourceNotFoundException;
import com.mishchuk.autotrade.exception.SourceNotFoundException;
import com.mishchuk.autotrade.mapper.CabinetSourceMapper;
import com.mishchuk.autotrade.repository.CabinetRepository;
import com.mishchuk.autotrade.repository.CabinetSourceRepository;
import com.mishchuk.autotrade.repository.SourceRepository;
import com.mishchuk.autotrade.repository.entity.CabinetEntity;
import com.mishchuk.autotrade.repository.entity.CabinetSourceEntity;
import com.mishchuk.autotrade.repository.entity.SourceEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CabinetSourceServiceImpl implements CabinetSourceService {

    private final CabinetSourceRepository cabinetSourceRepository;
    private final CabinetRepository cabinetRepository;
    private final SourceRepository sourceRepository;
    private final CabinetSourceMapper cabinetSourceMapper;

    @Override
    @Transactional
    public CabinetSourceDto addSourceToCabinet(UUID cabinetId, UUID sourceId) {

        log.info("Adding source {} to cabinet {}", sourceId, cabinetId);

        CabinetEntity cabinet = cabinetRepository.findById(cabinetId)
                .orElseThrow(() -> new CabinetNotFoundException("Cabinet with id " + cabinetId + " not found"));

        SourceEntity source = sourceRepository.findById(sourceId)
                .orElseThrow(() -> new SourceNotFoundException("Source with id " + sourceId + " not found"));

        CabinetSourceEntity entity = CabinetSourceEntity.builder()
                .cabinet(cabinet)
                .source(source)
                .status(Status.ACTIVE)
                .build();

        CabinetSourceEntity saved = cabinetSourceRepository.save(entity);

        log.info("Added source {} to cabinet {}", sourceId, cabinetId);

        return cabinetSourceMapper.toCabinetSourceDto(saved);
    }

    @Override
    @Transactional
    public CabinetSourceDto updateSourceStatus(UUID cabinetId, UUID sourceId, CabinetSourceStatusUpdateDto dto) {

        log.info("Updating status of source {} in cabinet {}", sourceId, cabinetId);

        CabinetSourceEntity entity = cabinetSourceRepository.findByCabinetIdAndSourceId(cabinetId, sourceId)
                .orElseThrow(() -> new CabinetSourceNotFoundException("Relation of source " + sourceId + " in cabinet " + cabinetId + " not found"));

        entity.setStatus(dto.getStatus());
        CabinetSourceEntity updated = cabinetSourceRepository.save(entity);

        log.info("Updated status of source {} in cabinet {}", sourceId, cabinetId);

        return cabinetSourceMapper.toCabinetSourceDto(updated);
    }

    @Override
    @Transactional
    public void removeSourceFromCabinet(UUID cabinetId, UUID sourceId) {

        log.info("Removing source {} from cabinet {}", sourceId, cabinetId);

        cabinetSourceRepository.deleteByCabinetIdAndSourceId(cabinetId, sourceId);

        log.info("Removed source {} from cabinet {}", sourceId, cabinetId);
    }

    @Override
    public List<CabinetSourceDto> getSourcesByCabinet(UUID cabinetId) {

        log.info("Fetching sources for cabinet {}", cabinetId);

        return cabinetSourceRepository.findByCabinetId(cabinetId).stream()
                .map(cabinetSourceMapper::toCabinetSourceDto)
                .toList();
    }
}