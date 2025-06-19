package com.mishchuk.autotrade.service;


import com.mishchuk.autotrade.controller.dto.SourceCreateDto;
import com.mishchuk.autotrade.controller.dto.SourceDetailDto;
import com.mishchuk.autotrade.controller.dto.SourceUpdateDto;

import java.util.UUID;

public interface SourceService {
    SourceDetailDto createSource(SourceCreateDto request);
    SourceDetailDto updateSource(UUID id, SourceUpdateDto request);
    SourceDetailDto getSource(UUID id);
    void deleteSource(UUID id);
}