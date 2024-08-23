package com.jt.mgen.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

    @NotBlank(message = "username cannot be blank and empty")
    private String username;
    @NotBlank(message = "password cannot be blank and empty")
    private String password;
}
