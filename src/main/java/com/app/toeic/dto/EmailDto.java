package com.app.toeic.dto;


import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailDto {
    @Email(message = "Địa chỉ email không hợp lệ")
    private String to;
}
