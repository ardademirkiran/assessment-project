package com.proksi.assessment.service;

import com.proksi.assessment.component.ApiUserDetails;
import com.proksi.assessment.dto.responseDto.ApiUserSignUpResponseDto;
import com.proksi.assessment.enums.ResponseStatus;
import com.proksi.assessment.repository.ApiUserRepository;
import com.proksi.assessment.enums.Role;
import com.proksi.assessment.dto.requestdto.ApiUserSignUpRequestDto;
import com.proksi.assessment.entity.ApiUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ApiUserService implements UserDetailsService {
    private final ApiUserRepository apiUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApiUserService(ApiUserRepository apiUserRepository, PasswordEncoder passwordEncoder) {
        this.apiUserRepository = apiUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ApiUser findByEmail(String email) {
        return apiUserRepository.findByEmail(email).orElseThrow();
    }
    @Transactional
    public ApiUserSignUpResponseDto createNewApiUser(ApiUserSignUpRequestDto requestDto) {
        ApiUserSignUpResponseDto responseDto = new ApiUserSignUpResponseDto();

        if (!requestDto.getPassword().equals(requestDto.getPasswordConfirm())) {
            responseDto.setStatus(ResponseStatus.FAILURE);
            return responseDto;
        }

        try {
            ApiUser apiUser = new ApiUser();
            apiUser.setEmail(requestDto.getEmail());
            apiUser.setPassword(passwordEncoder.encode(requestDto.getPassword()));
            apiUser.setRole(Role.USER);
            apiUserRepository.save(apiUser);

            responseDto.setId(apiUser.getId());
            responseDto.setEmail(requestDto.getEmail());
            responseDto.setStatus(ResponseStatus.SUCCESS);
            return responseDto;
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Email is already in use");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<ApiUser> targetUserOpt = apiUserRepository.findByEmail(username);
        if (targetUserOpt.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        ApiUser targetUser = targetUserOpt.get();
        return new ApiUserDetails(targetUser);
    }
}
