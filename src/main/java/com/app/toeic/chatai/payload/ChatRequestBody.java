package com.app.toeic.chatai.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRequestBody {
    ChatRequest chatRequest;
    String prompt;
}
