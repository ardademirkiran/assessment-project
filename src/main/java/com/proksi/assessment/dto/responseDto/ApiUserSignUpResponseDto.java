package com.proksi.assessment.dto.responseDto;

import com.proksi.assessment.enums.ResponseStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiUserSignUpResponseDto extends ResponseDto {
    private Long id;
    private String email;
    public ApiUserSignUpResponseDto(ResponseStatus responseStatus, String message) {
        super(responseStatus, message);
    }
}
