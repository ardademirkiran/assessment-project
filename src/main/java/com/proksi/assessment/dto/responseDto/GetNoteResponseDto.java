package com.proksi.assessment.dto.responseDto;

import com.proksi.assessment.entity.Note;
import com.proksi.assessment.enums.ResponseStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetNoteResponseDto extends ResponseDto {
    private Note note;

    public GetNoteResponseDto(ResponseStatus responseStatus, String message) {
        super(responseStatus, message);
    }
}
