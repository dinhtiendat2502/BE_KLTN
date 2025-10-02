package com.app.toeic.part.payload;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PartDTO {
    Integer examId;
    Integer partId;
    String partAudio;
}
