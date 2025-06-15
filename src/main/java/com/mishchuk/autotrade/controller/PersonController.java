package com.mishchuk.autotrade.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

public class PersonController {

    @GetMapping("/persons")
    public ResponseEntity<List<PersonDetailsDto>> getPersons() {
        // business logic: retrieve persons from database
        List<PersonDetailsDto> personsDto = new ArrayList<>();
        return new ResponseEntity<>(personsDto, HttpStatus.OK);
    }

    @GetMapping("/persons/{id}")
    public ResponseEntity<String> person(@PathVariable String id) {
        // business logic: retrieve one person from database
        return new ResponseEntity<>("Person: " + id, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> createPerson(@RequestBody PersonDetailsDto personDetailsDto) {
        // business logic: create something in database
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Void> updatePerson(@RequestBody PersonUpdateDto personUpdateDto) {
        // business logic: update something in database
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/persons/{id}")
    public ResponseEntity<String> deletePerson(@PathVariable String id) {
        // business logic: delete one person from database
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
