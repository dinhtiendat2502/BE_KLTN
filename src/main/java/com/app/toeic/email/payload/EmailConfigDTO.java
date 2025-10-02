package com.app.toeic.email.payload;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailConfigDTO {
    private Integer id;
    private String host;
    private int port;
    private String username;
    private String password;
}
