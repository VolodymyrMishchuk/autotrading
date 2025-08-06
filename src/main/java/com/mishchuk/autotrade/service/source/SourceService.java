package com.mishchuk.autotrade.service.source;

import com.mishchuk.autotrade.controller.dto.SourceCreateDto;
import com.mishchuk.autotrade.controller.dto.SourceDetailDto;
import com.mishchuk.autotrade.controller.dto.SourceUpdateDto;
import java.util.List;
import java.util.UUID;

public interface SourceService {
    SourceDetailDto createSource(SourceCreateDto dto);
    SourceDetailDto getSourceById(UUID  id);
    SourceDetailDto getSourceByToken(String token);
    List<SourceDetailDto> getAllSources();
    SourceDetailDto updateSource(UUID id, SourceUpdateDto dto);
    void deleteSource(UUID id);
}