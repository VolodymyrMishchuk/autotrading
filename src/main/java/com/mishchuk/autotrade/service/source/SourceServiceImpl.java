package com.mishchuk.autotrade.service.source;

import com.mishchuk.autotrade.controller.dto.SourceCreateDto;
import com.mishchuk.autotrade.controller.dto.SourceDetailDto;
import com.mishchuk.autotrade.controller.dto.SourceUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SourceServiceImpl implements SourceService {

    @Override
    public SourceDetailDto createSource(SourceCreateDto request) {
        // TODO: persist and return new source
        return new SourceDetailDto();
    }

    @Override
    public SourceDetailDto updateSource(UUID id, SourceUpdateDto request) {
        // TODO: update name, platform, token, status
        return new SourceDetailDto();
    }

    @Override
    public SourceDetailDto getSource(UUID id) {
        // TODO: find by ID
        return new SourceDetailDto();
    }

    @Override
    public void deleteSource(UUID id) {
        // TODO: soft/hard delete
    }
}