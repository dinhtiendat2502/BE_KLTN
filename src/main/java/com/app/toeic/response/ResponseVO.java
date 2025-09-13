package com.app.toeic.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String message;
    private Object data;
    private String status;
}
