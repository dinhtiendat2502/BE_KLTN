package com.app.toeic.user.payload;


import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserUpdateAvatarDTO {
    @NotEmpty(message = "Ảnh đại diện không được để trống")
    String avatar;
    Integer userId;
}
