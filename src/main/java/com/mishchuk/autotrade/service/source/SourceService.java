package com.mishchuk.autotrade.service.source;

import com.mishchuk.autotrade.service.model.Source;
import java.util.List;

public interface SourceService {
    void createSource(Source source);
    Source getSourceById(String id);
    Source getSourceByToken(String token);
    List<Source> getAllSources();
    void updateSource(Source source);
    void deleteSource(String id);
}