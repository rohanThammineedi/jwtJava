package com.jt.mgen.handler;

import com.jt.mgen.dto.ErrorDTO;
import com.jt.mgen.dto.JiraUserApplicationResponseDTO;
import com.jt.mgen.exception.JiraUserGlobalException;
import com.jt.mgen.exception.JiraUserNotFoundException;
import com.jt.mgen.exception.TokenExpiredException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class JiraExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public JiraUserApplicationResponseDTO<?> handleMethodArgumentException(MethodArgumentNotValidException ex) {
        JiraUserApplicationResponseDTO<?> response = new JiraUserApplicationResponseDTO<>();
        List<ErrorDTO> errorDTOList = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            ErrorDTO errorDTO = new ErrorDTO(fieldError.getField() + " : " + fieldError.getDefaultMessage());
            errorDTOList.add(errorDTO);
        });
        response.setErrors(errorDTOList);
        return response;
    }

    @ExceptionHandler(JiraUserGlobalException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public JiraUserApplicationResponseDTO<?> handleGlobalException(JiraUserGlobalException ex) {
        JiraUserApplicationResponseDTO<?> response = new JiraUserApplicationResponseDTO<>();
        List<ErrorDTO> errorDTOList = new ArrayList<>();
        errorDTOList.add(new ErrorDTO(ex.getMessage()));
        response.setErrors(errorDTOList);
        return response;
    }

    @ExceptionHandler(JiraUserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public JiraUserApplicationResponseDTO<?> handleUserNotFoundException(JiraUserNotFoundException ex) {
        JiraUserApplicationResponseDTO<?> response = new JiraUserApplicationResponseDTO<>();
        List<ErrorDTO> errorDTOList = new ArrayList<>();
        errorDTOList.add(new ErrorDTO(ex.getMessage()));
        response.setErrors(errorDTOList);
        return response;
    }

    @ExceptionHandler(TokenExpiredException.class)
    public JiraUserApplicationResponseDTO<?> handleTokenExpiredException(TokenExpiredException ex) {
        JiraUserApplicationResponseDTO<?> response = new JiraUserApplicationResponseDTO<>();
        List<ErrorDTO> errorDTOList = new ArrayList<>();
        errorDTOList.add(new ErrorDTO(ex.getMessage()));
        response.setErrors(errorDTOList);
        return response;
    }
}
