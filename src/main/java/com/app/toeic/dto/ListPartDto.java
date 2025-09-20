package com.app.toeic.dto;


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
