package com.app.toeic.user.payload;


import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class UserUpdateAvatarDTO implements Serializable {
    @NotEmpty(message = "Ảnh đại diện không được để trống")
    String avatar;
    Integer userId;
}
