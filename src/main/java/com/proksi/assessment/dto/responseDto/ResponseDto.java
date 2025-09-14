package com.proksi.assessment.dto.responseDto;

import com.proksi.assessment.enums.ResponseStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ResponseDto {
    private Result result;

    public ResponseDto(ResponseStatus responseStatus, String message) {
        result = new Result();
        result.setStatus(responseStatus);
        result.setResultMessage(message);
    }
}