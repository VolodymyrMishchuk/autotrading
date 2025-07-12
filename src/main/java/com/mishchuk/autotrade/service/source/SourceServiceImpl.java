package com.mishchuk.autotrade.service.source;

import com.mishchuk.autotrade.service.model.Source;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SourceServiceImpl implements SourceService {

    @Override
    public void createSource(Source source) {

    }

    @Override
    public Source getSource(String id) {
        return null;
    }

    @Override
    public List<Source> getAllSources() {
        return List.of();
    }

    @Override
    public void updateSource(Source source) {

    }

    @Override
    public void deleteSource(String id) {

    }
}