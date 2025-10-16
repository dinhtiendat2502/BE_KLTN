package com.app.toeic.firebase.controller;

import com.app.toeic.exception.AppException;
import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.firebase.repo.FirebaseUploadHistoryRepo;
import com.app.toeic.firebase.service.FirebaseStorageService;
import com.app.toeic.util.DatetimeUtils;
import com.app.toeic.util.FileSizeUtils;
import com.app.toeic.util.HttpStatus;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.Map;

@Log
@RestController
@RequestMapping("/firebase/history")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class FirebaseHistoryController {
    FirebaseUploadHistoryRepo firebaseUploadHistoryRepo;
    FirebaseStorageService firebaseStorageService;

    @GetMapping("/all")
    public Object getAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "desc") String sort,
            @RequestParam(value = "type", defaultValue = "all") String type,
            @RequestParam(value = "dateFrom", defaultValue = "") String dateFrom,
            @RequestParam(value = "dateTo", defaultValue = "") String dateTo
    ) {
        log.info(MessageFormat.format("FirebaseHistoryController >> getAll >> page: {0}, size: {1}, sort: {2}, type: {3}", page, size, sort, type));
        var sortRequest = "asc".equalsIgnoreCase(sort)
                ? Sort.by("uploadDate").ascending()
                : Sort.by("uploadDate").descending();
        var result = new Object();
        var totalSize = BigInteger.ZERO;
        var startDateTime = DatetimeUtils.getFromDate(dateFrom);
        var endDateTime = DatetimeUtils.getToDate(dateTo);
        if ("all".equalsIgnoreCase(type)) {
            result = firebaseUploadHistoryRepo.findAllByUploadDateBetween(startDateTime, endDateTime, PageRequest.of(page, size, sortRequest));
            totalSize = firebaseUploadHistoryRepo.sumByFileSizeAndUploadDateBetween(startDateTime, endDateTime);
        } else {
            result = firebaseUploadHistoryRepo.findAllByFileTypeAndUploadDateBetween(type, startDateTime, endDateTime, PageRequest.of(page, size, sortRequest));
            totalSize = firebaseUploadHistoryRepo.sumByFileSizeAndUploadDateBetween(type, startDateTime, endDateTime);
        }
        return ResponseVO
                .builder()
                .success(true)
                .data(
                        Map.of(
                                "totalSize", FileSizeUtils.convertByteToMB(totalSize),
                                "result", result
                        )
                )
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public Object delete(@PathVariable int id) {
        log.info(MessageFormat.format("FirebaseHistoryController >> delete >> id: {0}", id));
        var item = firebaseUploadHistoryRepo.findById(id);
        item.ifPresent(e -> {
            try {
                firebaseStorageService.delete(e.getFileName());
            } catch (IOException ex) {
                throw new AppException(HttpStatus.BAD_REQUEST, ex.getMessage());
            }
            firebaseUploadHistoryRepo.delete(e);
        });
        return ResponseVO
                .builder()
                .success(true)
                .message("DELETE_SUCCESS")
                .build();
    }
}
