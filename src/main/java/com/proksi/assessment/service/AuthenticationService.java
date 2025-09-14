package com.proksi.assessment.service;

import com.proksi.assessment.component.ApiUserDetails;
import com.proksi.assessment.constant.MessageConstants;
import com.proksi.assessment.entity.ApiUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements AuthenticationManager {

    private final PasswordEncoder passwordEncoder;
    private final ApiUserService apiUserService;

    public AuthenticationService(PasswordEncoder passwordEncoder, ApiUserService apiUserService) {
        this.passwordEncoder = passwordEncoder;
        this.apiUserService = apiUserService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        ApiUser targetUser = apiUserService.findByEmail(authentication.getName());
        if (targetUser == null) {
            throw new BadCredentialsException(MessageConstants.BAD_CREDENTIALS);
        }

        if (!passwordEncoder.matches(authentication.getCredentials().toString(), targetUser.getPassword())) {
            throw new BadCredentialsException(MessageConstants.BAD_CREDENTIALS);
        }

        UserDetails principal = new ApiUserDetails(targetUser);

        return new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
    }
}
