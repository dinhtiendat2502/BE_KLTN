package com.app.toeic.transcript.service;

import com.app.toeic.transcript.model.TranscriptHistory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Log
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class GoogleTranscriptService {
    @Async("crawlDataExecutor")
    public void getTranscript(MultipartFile file, TranscriptHistory transcriptHistory) {

    }
}
