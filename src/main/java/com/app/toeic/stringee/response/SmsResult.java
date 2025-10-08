package com.app.toeic.stringee.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmsResult {
    int price;
    int smsType;
    int r;
    String msg;
}
