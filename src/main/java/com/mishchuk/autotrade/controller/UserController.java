package com.mishchuk.autotrade.controller;

import com.mishchuk.autotrade.controller.dto.*;
import com.mishchuk.autotrade.mapper.UserMapper;
import com.mishchuk.autotrade.service.model.User;
import com.mishchuk.autotrade.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<UserDetailDto>> getUsers() {
        List<User> users = userService.getAllUsers();
        List<UserDetailDto> dtos = users
                .stream()
                .map(userMapper::toUserDetailDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<UserDetailDto> getUser(@PathVariable UUID id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(userMapper.toUserDetailDto(user));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@authHelper.isUserSelf(#id, principal.username) or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<UserDetailDto> updateUser(@PathVariable UUID id, @RequestBody @Valid UserUpdateDto dto) {
        User user = userService.getUserById(id);
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setBirthDate(dto.getBirthDate());
        userService.updateUser(user);
        return ResponseEntity.ok(userMapper.toUserDetailDto(user));
    }

    @PatchMapping("/{id}/password/change")
    @PreAuthorize("@authHelper.isUserSelf(#id, principal.username) or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> changePassword(
            @PathVariable UUID id,
            @RequestBody @Valid PasswordChangeDto body
    ) {
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

    @PatchMapping("/{id}/phone/change")
    @PreAuthorize("@authHelper.isUserSelf(#id, principal.username) or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<UserDetailDto> changePhone(
            @PathVariable UUID id,
            @RequestBody @Valid PhoneNumberChangeDto body
    ) {
        userService.changePhoneNumber(id, body.getOldPhoneNumber(), body.getNewPhoneNumber());
        return ResponseEntity.ok(userMapper.toUserDetailDto(userService.getUserById(id)));
    }

    @PostMapping("/phone/reset/request")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Void> requestPhoneReset(@RequestBody @Valid PhoneNumberResetRequestDto body) {
        userService.requestPhoneNumberReset(body.getEmail());
        return ResponseEntity.accepted().build(); // 202
    }

    @PostMapping("/phone/reset/complete")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Void> completePhoneReset(@RequestBody @Valid PhoneNumberResetCompleteDto body) {
        userService.completePhoneNumberReset(body.getToken(), body.getPhoneNumber());
        return ResponseEntity.noContent().build(); // 204
    }

    @PatchMapping("/{id}/email/change")
    @PreAuthorize("@authHelper.isUserSelf(#id, principal.username) or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<UserDetailDto> changeEmail(
            @PathVariable UUID id,
            @RequestBody @Valid EmailChangeDto body
    ) {
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

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<UserDetailDto> changeStatus(
            @PathVariable UUID id,
            @RequestBody @Valid StatusChangeDto body
    ) {
        userService.updateStatusOfUser(User.builder().id(id).status(body.getNewStatus()).build());
        return ResponseEntity.ok(userMapper.toUserDetailDto(userService.getUserById(id)));
    }

    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<UserDetailDto> changeRole(
            @PathVariable UUID id,
            @RequestBody @Valid RoleChangeDto body
    ) {
        userService.updateRoleOfUser(User.builder().id(id).role(body.getNewRole()).build());
        return ResponseEntity.ok(userMapper.toUserDetailDto(userService.getUserById(id)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}