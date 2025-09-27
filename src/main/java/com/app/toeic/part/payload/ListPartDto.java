package com.app.toeic.part.payload;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ListPartDto {
    Integer examId;
    String examName;
    List<String> listPart;
}
