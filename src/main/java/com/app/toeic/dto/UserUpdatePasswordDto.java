package com.app.toeic.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class UserUpdatePasswordDto implements Serializable {
    @NotEmpty(message = "Mật khẩu cũ không được bỏ trống")
    @Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự")
    String currentPassword;

    @NotEmpty(message = "Mật khẩu mới không được bỏ trống")
    @Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự")
    String newPassword;

    @NotEmpty(message = "Mật khẩu mới không được bỏ trống")
    @Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự")
    String confirmPassword;
}
