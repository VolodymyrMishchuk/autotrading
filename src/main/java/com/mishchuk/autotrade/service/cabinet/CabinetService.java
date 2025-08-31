package com.mishchuk.autotrade.service.cabinet;

import com.mishchuk.autotrade.controller.dto.cabinet.CabinetCreateDto;
import com.mishchuk.autotrade.controller.dto.cabinet.CabinetUpdateDto;
import com.mishchuk.autotrade.controller.dto.cabinet.CabinetDetailDto;
import java.util.List;
import java.util.UUID;

public interface CabinetService {

    CabinetDetailDto createCabinet(CabinetCreateDto dto);

    CabinetDetailDto updateCabinet(UUID cabinetId, CabinetUpdateDto dto);

    void deleteCabinet(UUID cabinetId);

    CabinetDetailDto getCabinetById(UUID cabinetId);

    List<CabinetDetailDto> getCabinetsByUserId(UUID userId);

    List<CabinetDetailDto> getCabinetsByAccountId(UUID accountId);
}