package com.proksi.assessment.dto.responseDto;

import com.proksi.assessment.enums.ResponseStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto extends ResponseDto {
    private String token;

    public LoginResponseDto(ResponseStatus responseStatus, String message) {
        super(responseStatus, message);
    }
}
