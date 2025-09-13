package com.app.toeic.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto implements Serializable {
    @NotBlank(message = "Username is required")
    private String username ;

    @NotBlank(message = "Password is required")
    @Min(value = 8, message = "Password must be at least 8 characters")
    private String password ;
}
