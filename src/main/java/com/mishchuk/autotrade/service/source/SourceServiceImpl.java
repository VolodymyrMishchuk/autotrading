package com.mishchuk.autotrade.service.source;

import com.mishchuk.autotrade.controller.dto.source.SourceCreateDto;
import com.mishchuk.autotrade.controller.dto.source.SourceDetailDto;
import com.mishchuk.autotrade.controller.dto.source.SourceUpdateDto;
import com.mishchuk.autotrade.exception.SourceNotFoundException;
import com.mishchuk.autotrade.mapper.SourceMapper;
import com.mishchuk.autotrade.repository.SourceRepository;
import com.mishchuk.autotrade.repository.entity.SourceEntity;
import com.mishchuk.autotrade.enums.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SourceServiceImpl implements SourceService {

    private final SourceRepository sourceRepository;
    private final SourceMapper sourceMapper;

    @Override
    public SourceDetailDto createSource(SourceCreateDto dto) {
        log.info("Creating new source: {}", dto.getName());
        SourceEntity entity = SourceEntity.builder()
                .name(dto.getName())
                .platform(dto.getPlatform())
                .status(Status.ACTIVE)
                .token(dto.getToken())
                .createdAt(Instant.now())
                .build();

        sourceRepository.save(entity);
        log.info("Source created: {}", entity.getId());
        return sourceMapper.toSourceDetailDto(entity);
    }

    @Override
    public SourceDetailDto getSourceById(UUID id) {
        SourceEntity entity = sourceRepository.findById(id)
                .orElseThrow(() -> new SourceNotFoundException("Source with id " + id + " not found"));
        return sourceMapper.toSourceDetailDto(entity);
    }

    @Override
    public SourceDetailDto getSourceByToken(String token) {
        SourceEntity entity = sourceRepository.findByToken(token)
                .orElseThrow(() -> new SourceNotFoundException("Source with token " + token + " not found"));
        return sourceMapper.toSourceDetailDto(entity);
    }

    @Override
    public List<SourceDetailDto> getAllSources() {
        return sourceRepository.findAll()
                .stream()
                .map(sourceMapper::toSourceDetailDto)
                .toList();
    }

    @Override
    public SourceDetailDto updateSource(UUID id, SourceUpdateDto dto) {
        SourceEntity entity = sourceRepository.findById(id)
                .orElseThrow(() -> new SourceNotFoundException("Source with id " + id + " not found"));

        if (dto.getName() != null) entity.setName(dto.getName());
        if (dto.getPlatform() != null) entity.setPlatform(dto.getPlatform());
        if (dto.getStatus() != null) entity.setStatus(dto.getStatus());
        if (dto.getToken() != null) entity.setToken(dto.getToken());
        entity.setUpdatedAt(Instant.now());

        sourceRepository.save(entity);

        log.info("Updated source: {}", id);
        return sourceMapper.toSourceDetailDto(entity);
    }

    @Override
    public void deleteSource(UUID id) {
        if (sourceRepository.existsById(id)) {
            sourceRepository.deleteById(id);
            log.info("Deleted source: {}", id);
        } else {
            throw new SourceNotFoundException("Source with id " + id + " not found");
        }
    }
}