package com.app.toeic.user.response;


import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class LoginResponse implements Serializable {
    String token;
    String email;
    List<String> roles;
}
