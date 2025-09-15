package com.proksi.assessment.exception;

import com.proksi.assessment.constant.MessageConstants;
import com.proksi.assessment.dto.responseDto.Result;
import com.proksi.assessment.enums.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@ControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException accessDeniedException) {
        Result result = new Result();
        result.setStatus(ResponseStatus.FAILURE);
        result.setResultMessage("This user is not authorized for this request.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException badCredentialsException) {
        Result result = new Result();
        result.setStatus(ResponseStatus.FAILURE);
        result.setResultMessage("Invalid credentials.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @ExceptionHandler(EmailConflictException.class)
    public ResponseEntity<?> handleEmailConflictException(EmailConflictException emailConflictException) {
        Result result = new Result();
        result.setStatus(ResponseStatus.FAILURE);
        result.setResultMessage(emailConflictException.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleException(Exception exc) {
        Result result = new Result();
        result.setStatus(ResponseStatus.FAILURE);
        result.setResultMessage(MessageConstants.GENERIC_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }
}