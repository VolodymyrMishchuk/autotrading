package com.mishchuk.autotrade.service.source;

import com.mishchuk.autotrade.exception.SourceNotFoundException;
import com.mishchuk.autotrade.mapper.SourceMapper;
import com.mishchuk.autotrade.repository.SourceRepository;
import com.mishchuk.autotrade.repository.entity.SourceEntity;
import com.mishchuk.autotrade.service.model.Source;
import com.mishchuk.autotrade.controller.dto.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SourceServiceImpl implements SourceService {

    private final SourceRepository sourceRepository;
    private final SourceMapper sourceMapper;

    @Override
    public void createSource(Source source) {

        log.info("Creating new source");

        source.setName(source.getName());
        source.setPlatform(source.getPlatform());
        source.setStatus(Status.ACTIVE);
        source.setToken(source.getToken());
        source.setCreatedAt(Instant.now());

        sourceRepository.save(sourceMapper.toSourceEntity(source));

        log.info("Source created: {}", source);
    }

    @Override
    public Source getSourceById(String id) {

        Optional<SourceEntity> optionalSourceEntity =
                sourceRepository.findById(UUID.fromString(id));

        if (optionalSourceEntity.isPresent()) {
            return  sourceMapper.toSource(optionalSourceEntity.get());
        }

        throw new SourceNotFoundException("Source with id " + id + " not found");
    }

    @Override
    public Source getSourceByToken(String token) {
        Optional<SourceEntity> optionalSourceEntity =
                sourceRepository.findById(UUID.fromString(token));

        if (optionalSourceEntity.isPresent()) {
            return  sourceMapper.toSource(optionalSourceEntity.get());
        }

        throw new SourceNotFoundException("Source with token " + token + " not found");
    }

    @Override
    public List<Source> getAllSources() {

        return sourceRepository
                .findAll()
                .stream()
                .map(sourceMapper::toSource)
                .toList();
    }

    @Override
    public void updateSource(Source source) {

        log.info("Updating user with id: {}", source.getId());

        Optional<SourceEntity> optionalSourceEntity =
                sourceRepository.findById(UUID.fromString(source.getId()));

        if (optionalSourceEntity.isPresent()) {
            SourceEntity sourceEntity = optionalSourceEntity.get();

            sourceEntity.setName(source.getName());
            sourceEntity.setPlatform(source.getPlatform());
            // думав добавити як окрему функцію зміну статусу
            // (активувати/деактивувати), але чи є в тому сенс?
            sourceEntity.setStatus(source.getStatus());
            sourceEntity.setToken(source.getToken());
            sourceEntity.setUpdatedAt(Instant.now());

            sourceRepository.save(sourceEntity);

            log.info("Updated user with id: {}", source.getId());
        } else {
            throw new SourceNotFoundException("Source with id " + source.getId() + " not found");
        }
    }

    @Override
    public void deleteSource(String id) {

        if (sourceRepository.existsById(UUID.fromString(id))) {
            sourceRepository.deleteById(UUID.fromString(id));
        } else {
            throw new SourceNotFoundException("Source with id " + id + " not found");
        }
    }
}