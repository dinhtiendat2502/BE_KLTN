package com.app.toeic.user.payload;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;



@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterDTO {
    @NotEmpty(message = "Email không được bỏ trống")
    @Email(message = "Email không hợp lệ")
    String email;

    @NotEmpty(message = "Mật khẩu không được bỏ trống")
    @Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự")
    String password;

    @NotEmpty(message = "Họ tên không được bỏ trống")
    String fullName;
}
