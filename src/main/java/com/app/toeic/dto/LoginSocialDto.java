package com.app.toeic.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class LoginSocialDto {
    @NotEmpty(message = "Email không được bỏ trống")
    @Email(message = "Email không hợp lệ")
    String email;
    @NotEmpty(message = "Họ tên không được bỏ trống")
    String fullName;
    String avatar;
    String provider;
}
