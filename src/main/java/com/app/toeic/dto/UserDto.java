package com.app.toeic.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private Integer id;
    private String email;
    private String status;
}
