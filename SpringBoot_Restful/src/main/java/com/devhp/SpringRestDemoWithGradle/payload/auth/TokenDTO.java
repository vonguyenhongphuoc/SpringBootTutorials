package com.devhp.SpringRestDemoWithGradle.payload.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class TokenDTO {
    private String token;
}
