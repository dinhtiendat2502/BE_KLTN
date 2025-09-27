package com.app.toeic.crawl.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class PartResponse {
    private Integer partId;
    private String partName;
    private String partCode;
    private String partImage;
    private String partAudio;
    private String partContent;
    private int numberOfQuestion;
    @Builder.Default
    private List<QuestionResponse> questions = new ArrayList<>();
}
