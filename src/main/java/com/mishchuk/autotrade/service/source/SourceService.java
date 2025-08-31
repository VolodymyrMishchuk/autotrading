package com.mishchuk.autotrade.service.source;

import com.mishchuk.autotrade.controller.dto.source.SourceCreateDto;
import com.mishchuk.autotrade.controller.dto.source.SourceDetailDto;
import com.mishchuk.autotrade.controller.dto.source.SourceUpdateDto;
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