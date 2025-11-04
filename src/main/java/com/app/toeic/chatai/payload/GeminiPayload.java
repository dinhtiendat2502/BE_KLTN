package com.app.toeic.chatai.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GeminiPayload {
    List<Content> contents;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Content {
        String role;
        List<PartContent> parts;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class PartContent {
        String text;
    }
}
