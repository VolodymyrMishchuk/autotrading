package com.mishchuk.autotrade.controller.dto;

import com.mishchuk.autotrade.enums.Status;
import lombok.Data;

@Data
public class AccountStatusUpdateDto {
    private Status status;
}
