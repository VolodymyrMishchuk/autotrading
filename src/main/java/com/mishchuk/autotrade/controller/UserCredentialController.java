package com.mishchuk.autotrade.controller;

import com.mishchuk.autotrade.controller.dto.user.*;
import com.mishchuk.autotrade.mapper.UserMapper;
import com.mishchuk.autotrade.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserCredentialController {

    private final UserService userService;
    private final UserMapper userMapper;

    // ===== PASSWORD =====

    @PatchMapping("/{id}/password/change")
    @PreAuthorize("@authHelper.isUserSelf(#id, principal.username) or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> changePassword(@PathVariable UUID id, @RequestBody @Valid PasswordChangeDto body) {
        userService.changePassword(id, body.getOldPassword(), body.getNewPassword());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/password/reset/request")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Void> requestPasswordReset(@RequestBody @Valid PasswordResetRequestDto body) {
        userService.requestPasswordReset(body.getEmail());
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/password/reset/complete")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Void> completePasswordReset(@RequestBody @Valid PasswordResetCompleteDto body) {
        userService.completePasswordReset(body.getToken(), body.getPassword());
        return ResponseEntity.noContent().build();
    }

    // ===== PHONE =====

    @PatchMapping("/{id}/phone/change")
    @PreAuthorize("@authHelper.isUserSelf(#id, principal.username) or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<UserDetailDto> changePhone(@PathVariable UUID id, @RequestBody @Valid PhoneNumberChangeDto body) {
        userService.changePhoneNumber(id, body.getOldPhoneNumber(), body.getNewPhoneNumber());
        return ResponseEntity.ok(userMapper.toUserDetailDto(userService.getUserById(id)));
    }

    @PostMapping("/phone/reset/request")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Void> requestPhoneReset(@RequestBody @Valid PhoneNumberResetRequestDto body) {
        userService.requestPhoneNumberReset(body.getEmail());
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/phone/reset/complete")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Void> completePhoneReset(@RequestBody @Valid PhoneNumberResetCompleteDto body) {
        userService.completePhoneNumberReset(body.getToken(), body.getPhoneNumber());
        return ResponseEntity.noContent().build();
    }

    // ===== EMAIL =====

    @PatchMapping("/{id}/email/change")
    @PreAuthorize("@authHelper.isUserSelf(#id, principal.username) or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<UserDetailDto> changeEmail(@PathVariable UUID id, @RequestBody @Valid EmailChangeDto body) {
        userService.changeEmail(id, body.getOldEmail(), body.getNewEmail());
        return ResponseEntity.ok(userMapper.toUserDetailDto(userService.getUserById(id)));
    }

    @PostMapping("/email/reset/request")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Void> requestEmailReset(@RequestBody @Valid EmailResetRequestDto body) {
        userService.requestEmailResetByPhone(body.getPhoneNumber());
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/email/reset/complete")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Void> completeEmailReset(@RequestBody @Valid EmailResetCompleteDto body) {
        userService.completeEmailResetBySms(body.getVerificationCodeBySMS(), body.getEmail());
        return ResponseEntity.noContent().build();
    }
}