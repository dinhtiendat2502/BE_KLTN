package com.app.toeic.komunicate.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KommunicateAccountDTO {
    private Integer id;
    private String email;
    private String password;
}
