package com.app.toeic.exam.payload;


public record ExamDTO(Integer examId, String examName, String examImage, String audioPart1, String audioPart2,
                      String audioPart3, String audioPart4, Integer topicId, boolean isFree) {
    public ExamDTO {
        if (examName == null || examName.isEmpty()) {
            throw new IllegalArgumentException("Tên đề thi không được để trống");
        }
    }
}
