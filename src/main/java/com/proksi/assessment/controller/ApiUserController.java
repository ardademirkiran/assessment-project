package com.proksi.assessment.controller;

import com.proksi.assessment.dto.requestdto.ApiUserSignUpRequestDto;
import com.proksi.assessment.dto.responseDto.ApiUserSignUpResponseDto;
import com.proksi.assessment.service.ApiUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api-user")
public class ApiUserController {

    private final ApiUserService apiUserService;

    @Autowired
    public ApiUserController(ApiUserService apiUserService) {
        this.apiUserService = apiUserService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody ApiUserSignUpRequestDto apiUserSignUpRequestDto) {
        ApiUserSignUpResponseDto responseDto = apiUserService.createNewApiUser(apiUserSignUpRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}
