package com.jt.mgen.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponseDto {

    private String accessToken;
    private String refreshToken;

    public JwtResponseDto(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
