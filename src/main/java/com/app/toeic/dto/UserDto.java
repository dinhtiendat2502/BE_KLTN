package com.app.toeic.dto;


import com.app.toeic.enums.EUser;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private Integer id;
    private String email;
    private EUser status;
}
