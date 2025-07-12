package com.mishchuk.autotrade.service.source;

import com.mishchuk.autotrade.controller.dto.SourceUpdateDto;
import com.mishchuk.autotrade.service.model.Source;
import java.util.List;
import java.util.UUID;

public interface SourceService {
    void createSource(Source source);
    Source getSource(String id);
    List<Source> getAllSources();
    void updateSource(Source source);
    void deleteSource(String id);
}