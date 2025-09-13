package com.proksi.assessment.dto.requestdto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiUserSignUpRequestDto {
    private String email;
    private String password;
    private String passwordConfirm;
}
