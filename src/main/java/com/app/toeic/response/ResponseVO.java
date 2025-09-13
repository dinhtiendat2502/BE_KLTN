package com.app.toeic.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseVO implements Serializable {
    private Boolean success;
    private Object data;
    private String message;
}
