package com.app.toeic.send_sms.payload;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmsPayload {
    @Builder.Default
    List<Sms> sms = new ArrayList<>();
}
