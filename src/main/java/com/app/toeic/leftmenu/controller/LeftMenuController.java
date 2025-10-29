package com.app.toeic.leftmenu.controller;

import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.leftmenu.repo.MenuGroupRepo;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/left-menu")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class LeftMenuController {
    MenuGroupRepo menuGroupRepo;

    @GetMapping("get")
    public Object getLeftMenu(){
        return ResponseVO
                .builder()
                .success(true)
                .data(menuGroupRepo.findAll())
                .message("Get left menu success")
                .build();
    }
}
