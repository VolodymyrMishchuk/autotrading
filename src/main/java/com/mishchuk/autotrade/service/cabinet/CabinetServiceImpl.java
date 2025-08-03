package com.mishchuk.autotrade.service.cabinet;

import com.mishchuk.autotrade.controller.dto.CabinetCreateDto;
import com.mishchuk.autotrade.controller.dto.CabinetDetailDto;
import com.mishchuk.autotrade.controller.dto.CabinetUpdateDto;
import com.mishchuk.autotrade.exception.AccountNotFoundException;
import com.mishchuk.autotrade.exception.CabinetNotFoundException;
import com.mishchuk.autotrade.exception.UserNotFoundException;
import com.mishchuk.autotrade.mapper.CabinetMapper;
import com.mishchuk.autotrade.repository.AccountRepository;
import com.mishchuk.autotrade.repository.CabinetRepository;
import com.mishchuk.autotrade.repository.SourceRepository;
import com.mishchuk.autotrade.repository.UserRepository;
import com.mishchuk.autotrade.repository.entity.*;
import com.mishchuk.autotrade.service.model.Cabinet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CabinetServiceImpl implements CabinetService {

    private final CabinetRepository cabinetRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final SourceRepository sourceRepository;
    private final CabinetMapper cabinetMapper;

    @Override
    @Transactional
    public CabinetDetailDto createCabinet(CabinetCreateDto dto) {
        log.info("Creating new cabinet");

        UUID userId = dto.getUserId();
        UUID accountId = dto.getAccountId();

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));

        AccountEntity account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account with id " + accountId + " not found"));

        List<SourceEntity> sources = sourceRepository.findAllById(dto.getSourceIds());

        Cabinet cabinet = CabinetMapper.toCabinet(userId, dto);
        CabinetEntity entity = CabinetMapper.toCabinetEntity(cabinet, user, account, sources);
        CabinetEntity saved = cabinetRepository.save(entity);

        log.info("Cabinet created: {}", cabinet);

        return CabinetMapper.toCabinetDetailDto(CabinetMapper.toCabinet(saved));
    }

    @Override
    @Transactional
    public CabinetDetailDto updateCabinet(UUID cabinetId, CabinetUpdateDto dto) {
        log.info("Updating cabinet with id: {}", cabinetId);

        CabinetEntity entity = cabinetRepository.findById(cabinetId)
                .orElseThrow(() -> new CabinetNotFoundException("Cabinet with id " + cabinetId + " not found"));

        Cabinet current = CabinetMapper.toCabinet(entity);
        Cabinet updated = CabinetMapper.mergeCabinet(current, CabinetMapper.toCabinet(dto));

        UserEntity user = entity.getUser();
        AccountEntity account = entity.getAccount();
        List<SourceEntity> sources = dto.getSourceIds() != null
                ? sourceRepository.findAllById(dto.getSourceIds())
                : entity.getSources().stream()
                .map(CabinetSourceEntity::getSource)
                .toList();

        CabinetEntity toSave = CabinetMapper.toCabinetEntity(updated, user, account, sources);
        CabinetEntity saved = cabinetRepository.save(toSave);

        log.info("Updated cabinet with id: {}", cabinetId);

        return CabinetMapper.toCabinetDetailDto(CabinetMapper.toCabinet(saved));
    }

    @Override
    public void deleteCabinet(UUID cabinetId) {
        log.info("Removing cabinet with id: {}", cabinetId);
        cabinetRepository.deleteById(cabinetId);
        log.info("Removed cabinet with id: {}", cabinetId);
    }

    @Override
    public CabinetDetailDto getCabinetById(UUID cabinetId) {
        CabinetEntity entity = cabinetRepository.findById(cabinetId)
                .orElseThrow(() -> new CabinetNotFoundException("Cabinet with id " + cabinetId + " not found"));
        return CabinetMapper.toCabinetDetailDto(CabinetMapper.toCabinet(entity));
    }

    @Override
    public List<CabinetDetailDto> getCabinetsByUserId(UUID userId) {
        return cabinetRepository.findByUser_Id(userId).stream()
                .map(CabinetMapper::toCabinet)
                .map(CabinetMapper::toCabinetDetailDto)
                .toList();
    }

    @Override
    public List<CabinetDetailDto> getCabinetsByAccountId(UUID accountId) {
        return cabinetRepository.findByAccount_Id(accountId).stream()
                .map(CabinetMapper::toCabinet)
                .map(CabinetMapper::toCabinetDetailDto)
                .toList();
    }
}