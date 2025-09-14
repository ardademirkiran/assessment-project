package com.proksi.assessment.dto.responseDto;

import com.proksi.assessment.enums.ResponseStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Result {
    private ResponseStatus status;
    private String resultMessage;
}
