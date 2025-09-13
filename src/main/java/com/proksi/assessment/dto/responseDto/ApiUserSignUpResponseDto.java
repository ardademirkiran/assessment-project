package com.proksi.assessment.dto.responseDto;

import com.proksi.assessment.enums.ResponseStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiUserSignUpResponseDto {
    private Long id;
    private String email;
    private ResponseStatus status;
}
