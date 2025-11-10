package com.app.toeic.course.controller;

import com.app.toeic.course.model.CourseRegister;
import com.app.toeic.course.repo.CourseRegisterRepo;
import com.app.toeic.external.response.ResponseVO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/course-register")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CourseRegisterController {
    CourseRegisterRepo courseRegisterRepo;

    @PostMapping("create")
    public Object registerCourse(@RequestBody CourseRegisterRequest request) {
        var course = CourseRegister.builder()
                                   .name(request.name())
                                   .phone(request.phone())
                                   .email(request.email())
                                   .build();
        courseRegisterRepo.save(course);
        return ResponseVO
                .builder()
                .success(true)
                .build();
    }

    @PatchMapping("confirm/{id}")
    public Object confirmCourse(@PathVariable Integer id) {
        var course = courseRegisterRepo.findById(id);
        course.ifPresent(e -> {
            e.setStatus(true);
            courseRegisterRepo.save(e);
        });
        return ResponseVO
                .builder()
                .success(true)
                .build();
    }

    @GetMapping("all")
    public Object getAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        var pageRequest = PageRequest.of(page, size);
        return courseRegisterRepo.findAllOrderByCreatedDate(pageRequest);
    }

    public record CourseRegisterRequest(String name, String phone, String email) {
    }
}
