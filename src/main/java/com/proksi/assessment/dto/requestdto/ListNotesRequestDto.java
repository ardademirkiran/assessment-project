package com.proksi.assessment.dto.requestdto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListNotesRequestDto {
    private Integer pageNum;
    private Integer size;
    private Long userId;
}
