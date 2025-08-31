package com.mishchuk.autotrade.controller;

import com.mishchuk.autotrade.controller.dto.RoleChangeDto;
import com.mishchuk.autotrade.controller.dto.StatusChangeDto;
import com.mishchuk.autotrade.controller.dto.UserDetailDto;
import com.mishchuk.autotrade.mapper.UserMapper;
import com.mishchuk.autotrade.service.model.User;
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
public class UserAdminController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<UserDetailDto> changeStatus(@PathVariable UUID id, @RequestBody @Valid StatusChangeDto body) {
        userService.updateStatusOfUser(User.builder().id(id).status(body.getNewStatus()).build());
        return ResponseEntity.ok(userMapper.toUserDetailDto(userService.getUserById(id)));
    }

    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<UserDetailDto> changeRole(@PathVariable UUID id, @RequestBody @Valid RoleChangeDto body) {
        userService.updateRoleOfUser(User.builder().id(id).role(body.getNewRole()).build());
        return ResponseEntity.ok(userMapper.toUserDetailDto(userService.getUserById(id)));
    }
}