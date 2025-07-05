package com.mishchuk.autotrade.controller;

import com.mishchuk.autotrade.controller.dto.UserDetailDto;
import com.mishchuk.autotrade.controller.dto.UserUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    @GetMapping
    public ResponseEntity<List<UserDetailDto>> getUsers() {
        // business logic: retrieve persons from a database
        List<UserDetailDto> usersDto = new ArrayList<>();
        return new ResponseEntity<>(usersDto, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> user(@PathVariable String id) {
        // business logic: retrieve one person from a database
        return new ResponseEntity<>("User: " + id, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody UserDetailDto userDetailDto) {
        // business logic: create something in a database
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable UUID id, @RequestBody UserUpdateDto userUpdateDto) {
        // business logic: update something in a database
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        // business logic: delete one person from a database
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
