package com.mishchuk.autotrade.controller;

import com.mishchuk.autotrade.repository.RawSignalRepository;
import com.mishchuk.autotrade.repository.entity.RawSignalEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/signals")
@RequiredArgsConstructor
@CrossOrigin
public class SignalController {

    private final RawSignalRepository rawSignalRepository;

    @GetMapping("/raw")
    public List<RawSignalEntity> getAllRawSignals() {
        return rawSignalRepository.findAll();
    }
}
