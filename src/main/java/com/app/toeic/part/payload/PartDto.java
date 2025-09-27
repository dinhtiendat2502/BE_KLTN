package com.app.toeic.part.payload;


import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class PartDto implements Serializable {
    Integer examId;
    Integer partId;
    String partAudio;
}
