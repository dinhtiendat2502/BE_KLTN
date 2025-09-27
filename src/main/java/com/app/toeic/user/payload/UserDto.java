package com.app.toeic.user.payload;


import com.app.toeic.user.enums.EUser;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    Integer id;
    EUser status;
}
