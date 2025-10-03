package com.app.toeic.chatai.payload;

import com.app.toeic.chatai.model.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class ChatRequest {
    String model;

    @Builder.Default
    List<Message> messages = new ArrayList<>();
    public ChatRequest() {
        this.model = "gpt-3.5-turbo";
        this.messages = new ArrayList<>();
        this.messages.add(new Message("system", "You are a helpful assistant."));
    }
    public void addMessage(Message message) {
        this.messages.add(message);
    }
}
