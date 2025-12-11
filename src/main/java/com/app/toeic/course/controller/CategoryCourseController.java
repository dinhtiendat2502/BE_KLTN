package com.app.toeic.course.controller;

import com.app.toeic.course.model.CategoryCourse;
import com.app.toeic.course.repo.CategoryCourseRepo;
import com.app.toeic.exception.AppException;
import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.util.Constant;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category-course")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CategoryCourseController {
    CategoryCourseRepo categoryCourseRepo;

    @PostMapping("upsert")
    @Transactional
    public Object upsertCategoryCourse(@RequestBody CategoryCourseRequest request) {
        var categoryCourseOptional = categoryCourseRepo.findById(request.categoryId());
        categoryCourseOptional.ifPresentOrElse(e -> {
            e.setName(request.categoryName());
            e.setPosition(request.position());
            categoryCourseRepo.save(e);
        }, () -> {
            var lastPosition = categoryCourseRepo.findFirstByOrderByPositionDesc()
                                                 .map(CategoryCourse::getPosition)
                                                 .orElse(0);
            var categoryCourse = CategoryCourse
                    .builder()
                    .name(request.categoryName())
                    .position(lastPosition + 1)
                    .build();
            categoryCourseRepo.save(categoryCourse);
        });
        return ResponseVO.builder().success(true).build();
    }

    @GetMapping("/all")
    public Object getAll() {
        return ResponseVO
                .builder()
                .success(true)
                .data(categoryCourseRepo.findAllOrderByPosition())
                .build();
    }

    @GetMapping("all-v2")
    public Object getAllV2() {
        return ResponseVO
                .builder()
                .success(true)
                .data(categoryCourseRepo.findAllOrderByPosition(Constant.STATUS_ACTIVE))
                .build();
    }

    @PatchMapping("update-position")
    @Transactional
    public Object updatePosition(@RequestBody UpdatePositionRequest request) {
        var categoryCourseOptional = categoryCourseRepo.findById(request.categoryId());
        var categoryCourse = categoryCourseOptional.orElseThrow(() -> new AppException().fail("Category not found"));
        var position = request.position() + categoryCourse.getPosition();
        categoryCourse.setPosition(Math.max(position, 0));
        categoryCourseRepo.save(categoryCourse);
        return ResponseVO.builder().success(true).build();
    }

    @DeleteMapping("delete/{categoryId}")
    @Transactional
    public Object deleteCategoryCourse(@PathVariable Integer categoryId) {
        categoryCourseRepo.findById(categoryId).ifPresent(categoryCourse -> {
            categoryCourse.setStatus(Constant.STATUS_INACTIVE);
            categoryCourseRepo.save(categoryCourse);
        });
        return ResponseVO.builder().success(true).build();
    }

    public record CategoryCourseRequest(
            Integer categoryId,
            String categoryName,
            Integer position) {
    }

    public record UpdatePositionRequest(
            Integer categoryId,
            Integer position) {
    }
}
