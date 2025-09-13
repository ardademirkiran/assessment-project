package com.proksi.assessment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/note")
public class NoteController {

    @PostMapping("/try")
    public ResponseEntity<String> tryNote() {
        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }
}
