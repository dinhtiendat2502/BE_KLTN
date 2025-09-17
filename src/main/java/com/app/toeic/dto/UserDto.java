package com.app.toeic.dto;


import com.app.toeic.enums.EUser;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    Integer id;
    EUser status;
}
