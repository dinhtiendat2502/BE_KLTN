package com.app.toeic.chatai.payload;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GeminiPayload {

    @Builder.Default
    List<Content> contents = new ArrayList<>();

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Content {
        String role;

        @Builder.Default
        List<PartContent> parts = new ArrayList<>();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PartContent {
        String text;
    }
}
