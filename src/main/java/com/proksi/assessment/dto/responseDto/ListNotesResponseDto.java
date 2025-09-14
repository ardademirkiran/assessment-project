package com.proksi.assessment.dto.responseDto;

import com.proksi.assessment.entity.Note;
import com.proksi.assessment.enums.ResponseStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListNotesResponseDto extends ResponseDto {
    private List<Note> notes;

    public ListNotesResponseDto(ResponseStatus responseStatus, String message) {
        super(responseStatus, message);
    }
}
