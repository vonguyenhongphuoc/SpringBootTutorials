package com.devhp.SpringRestDemoWithGradle.payload.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PasswordDTO {
    @Size(min = 6, max = 20)
    @Schema(description = "Password", example = "Password")
    private String password;
}
