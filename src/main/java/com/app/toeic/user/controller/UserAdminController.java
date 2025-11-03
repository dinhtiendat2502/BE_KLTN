package com.app.toeic.user.controller;


import com.app.toeic.user.payload.UserDTO;
import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.user.repo.IUserAccountRepository;
import com.app.toeic.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/user")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class UserAdminController {
    UserService userService;
    IUserAccountRepository iUserAccountRepository;

    @GetMapping("/list")
    public Object getAllUser(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "status", defaultValue = "ALL") String status
    ) {
        if ("ALL".equalsIgnoreCase(status)) {
            return iUserAccountRepository.findAllUser(PageRequest.of(page, size));
        }
        return iUserAccountRepository.findAllUserByStatus(status, PageRequest.of(page, size));
    }

    @PostMapping("/update-user")
    public ResponseVO updateUser(@RequestBody UserDTO user) {
        return userService.updateUser(user);
    }

    @GetMapping("activity")
    public Object getActivities(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "type", defaultValue = "ALL") String type
    ) {
        return userService.getActivities(page, size, type);
    }
}
