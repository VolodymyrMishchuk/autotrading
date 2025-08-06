package com.mishchuk.autotrade.controller;

import com.mishchuk.autotrade.controller.dto.UserCreateDto;
import com.mishchuk.autotrade.controller.dto.UserDetailDto;
import com.mishchuk.autotrade.controller.dto.UserUpdateDto;
import com.mishchuk.autotrade.mapper.UserMapper;
import com.mishchuk.autotrade.service.model.User;
import com.mishchuk.autotrade.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
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

    @PostMapping
    public ResponseEntity<UserDetailDto> createUser(@RequestBody @Valid UserCreateDto dto, UriComponentsBuilder uriBuilder) {
        User user = userMapper.toUser(dto);
        userService.createUser(user);
        var location = uriBuilder.path("/users/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(location).body(userMapper.toUserDetailDto(user));
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

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
