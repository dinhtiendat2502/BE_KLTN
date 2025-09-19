package com.app.toeic.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class UserUpdateDto implements Serializable {
    @Email(message = "Email không hợp lệ")
    String email;

    @NotEmpty(message = "Mật khẩu không được bỏ trống")
    @Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự")
    String password;

    @NotEmpty(message = "Họ tên không được bỏ trống")
    String fullName;
}
