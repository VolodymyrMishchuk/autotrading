package com.mishchuk.autotrade.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    //@GetMapping("/health")
    // /health
    /*
    public ResponseEntity<String> health() {
        String message = "Server is up and running";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(message, HttpStatus.OK);
        return responseEntity;
        // return responseEntity.ok("You are health")
    }
    */


    // /health?number=1(2)
    @GetMapping("/health")
    public ResponseEntity<String> health(@RequestParam int number) {
        if (number == 1) {
            String message = "Server is up and running";
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            String message = "All is bad!";
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    // /gesundheit/{id}
    @GetMapping("/gesundheit/{id}")
    public ResponseEntity<String> health(@PathVariable String id) {
        return new ResponseEntity<>("Gesundheit: " + id, HttpStatus.OK);
    }
}
