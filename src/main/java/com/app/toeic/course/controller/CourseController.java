package com.app.toeic.course.controller;

import com.app.toeic.course.model.Course;
import com.app.toeic.course.payload.CourseRequest;
import com.app.toeic.course.repo.CategoryCourseRepo;
import com.app.toeic.course.repo.CourseRepo;
import com.app.toeic.external.model.SystemConfig;
import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.external.service.SystemConfigService;
import com.app.toeic.firebase.service.FirebaseStorageService;
import com.app.toeic.util.Constant;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/course")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CourseController {
    FirebaseStorageService firebaseStorageService;
    CourseRepo courseRepo;
    CategoryCourseRepo categoryCourseRepo;
    SystemConfigService systemConfigService;
    FirebaseStorageService systemConfig;

    @GetMapping("qr-code")
    public Object getQrCode() {
        return ResponseVO.builder().success(true).data(systemConfigService.getConfigValue(Constant.QR_CODE)).build();
    }

    @GetMapping("detail/{id}")
    public Object getCourseDetail(@PathVariable Integer id) {
        return ResponseVO.builder().success(true).data(courseRepo.findDetailByCourseId(id)).build();
    }

    @PatchMapping("update-qr")
    public Object updateQrCode(@RequestParam("qr") MultipartFile qr) throws IOException {
        var image = firebaseStorageService.uploadFile(qr);
        systemConfigService.updateConfigValue(Constant.QR_CODE, image);
        return ResponseVO.builder().success(true).build();
    }

    @PostMapping("upsert")
    @Transactional(rollbackFor = IOException.class)
    public Object upsertCourse(@ModelAttribute CourseRequest request) throws IOException {
        String img = "";
        if (request.file() != null) {
            img = firebaseStorageService.uploadFile(request.file());
        }
        final var image = img;

        var courseOpt = courseRepo.findById(request.courseId());
        var categoryOpt = categoryCourseRepo.findById(request.categoryCourseId());
        courseOpt.ifPresentOrElse(e -> {
            e.withUpdate(request);
            e.setThumbnail(StringUtils.defaultIfBlank(image, e.getThumbnail()));
            e.setCategoryCourse(categoryOpt.orElse(null));
            courseRepo.save(e);
        }, () -> {
            var course = Course.from(request);
            course.setThumbnail(image);
            course.setCategoryCourse(categoryOpt.orElse(null));
            courseRepo.save(course);
        });
        return ResponseVO.builder().success(true).build();
    }

    @GetMapping("/all")
    public Object getAll() {
        return ResponseVO.builder().success(true).data(courseRepo.findAllByStatus(Constant.STATUS_ACTIVE)).build();
    }

    @DeleteMapping("/delete/{id}")
    public Object deleteCourse(@PathVariable Integer id) {
        courseRepo.findById(id).ifPresent(course -> {
            course.setStatus(Constant.STATUS_INACTIVE);
            courseRepo.save(course);
        });
        return ResponseVO.builder().success(true).build();
    }

}
