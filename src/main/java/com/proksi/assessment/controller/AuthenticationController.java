package com.proksi.assessment.controller;

import com.proksi.assessment.component.ApiUserDetails;
import com.proksi.assessment.dto.requestdto.LoginRequestDto;
import com.proksi.assessment.dto.responseDto.LoginResponseDto;
import com.proksi.assessment.enums.ResponseStatus;
import com.proksi.assessment.service.ApiUserService;
import com.proksi.assessment.service.AuthenticationService;
import com.proksi.assessment.service.JwtUtilService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtUtilService jwtUtilService;

    public AuthenticationController(AuthenticationService authenticationService, JwtUtilService jwtUtilService) {
        this.authenticationService = authenticationService;
        this.jwtUtilService = jwtUtilService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) {
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);

        try {
            Authentication auth = authenticationService.authenticate(authenticationToken);
            ApiUserDetails principal = (ApiUserDetails) auth.getPrincipal();
            String jwtToken = jwtUtilService.generateToken(principal, principal.getId());
            LoginResponseDto loginResponseDto = new LoginResponseDto(jwtToken, ResponseStatus.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(loginResponseDto);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Bad credentials.");
        }

    }
}
