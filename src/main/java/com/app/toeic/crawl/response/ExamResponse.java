package com.app.toeic.crawl.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class ExamResponse {
    private Integer examId;
    private String examName;
    private String examImage;

    private String examAudio;
    private String audioPart1;
    private String audioPart2;
    private String audioPart3;
    private String audioPart4;
    private String status;
    private Integer numberOfUserDoExam;
    private Double price;

    @Builder.Default
    private List<PartResponse> parts = new ArrayList<>();
}
