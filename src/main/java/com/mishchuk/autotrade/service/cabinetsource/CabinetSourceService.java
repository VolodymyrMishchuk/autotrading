package com.mishchuk.autotrade.service.cabinetsource;

import com.mishchuk.autotrade.controller.dto.CabinetSourceDto;
import com.mishchuk.autotrade.controller.dto.CabinetSourceStatusUpdateDto;

import java.util.List;
import java.util.UUID;

public interface CabinetSourceService {

    CabinetSourceDto addSourceToCabinet(UUID cabinetId, UUID sourceId);

    CabinetSourceDto updateSourceStatus(UUID cabinetId, UUID sourceId, CabinetSourceStatusUpdateDto dto);

    void removeSourceFromCabinet(UUID cabinetId, UUID sourceId);

    List<CabinetSourceDto> getSourcesByCabinet(UUID cabinetId);
}