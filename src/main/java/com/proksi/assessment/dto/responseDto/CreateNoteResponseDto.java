package com.proksi.assessment.dto.responseDto;

import com.proksi.assessment.enums.ResponseStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateNoteResponseDto extends ResponseDto {
    private Long noteId;

    public CreateNoteResponseDto(ResponseStatus responseStatus, String message) {
        super(responseStatus, message);
    }
}
