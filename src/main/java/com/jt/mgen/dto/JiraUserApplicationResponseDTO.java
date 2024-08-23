package com.jt.mgen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JiraUserApplicationResponseDTO<T> {

    private T response;
    private List<ErrorDTO> errors;

    public JiraUserApplicationResponseDTO(List<ErrorDTO> errors) {
        this.errors = errors;
    }
}
