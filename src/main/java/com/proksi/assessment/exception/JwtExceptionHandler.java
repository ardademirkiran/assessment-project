package com.proksi.assessment.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proksi.assessment.dto.responseDto.Result;
import com.proksi.assessment.enums.ResponseStatus;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtExceptionHandler {

    ObjectMapper objectMapper = new ObjectMapper();

    public void handelJwtException(HttpServletResponse httpServletResponse) throws IOException {
        Result result = new Result();
        result.setResultMessage("JWT token invalid.");
        result.setStatus(ResponseStatus.FAILURE);
        httpServletResponse.setStatus(401);
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(httpServletResponse.getWriter(), result);
    }
}
