package com.app.toeic.stringee.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CallPayload {
    CallFromPayload from;
    List<CallToPayload> to;
    String answer_url;
    List<CallActionPayload> actions;
}
