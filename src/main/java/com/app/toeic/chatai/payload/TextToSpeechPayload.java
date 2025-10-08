package com.app.toeic.chatai.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TextToSpeechPayload {
    String model;
    String input;
    String voice;
}
