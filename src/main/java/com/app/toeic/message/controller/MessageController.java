package com.app.toeic.message.controller;

import com.app.toeic.exception.AppException;
import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.message.repo.ConversationRepo;
import com.app.toeic.user.model.UserAccount;
import com.app.toeic.user.repo.IUserAccountRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class MessageController {
    IUserAccountRepository iUserAccountRepository;
    ConversationRepo conversationRepo;
    @PostMapping("create-conversation")
    public Object createConversation(
            @RequestBody CreateChannelPayload payload,
            @AuthenticationPrincipal UserAccount userAccount
    ) {
        var rs = ResponseVO.builder().build();
        if (userAccount == null) {
            rs.setSuccess(false);
            rs.setData("User not found");
            return rs;
        }

        var user = iUserAccountRepository.findByEmail(payload.email())
                                         .orElseThrow(() -> new AppException().fail("User not found"));

        return rs;
    }

    public record CreateChannelPayload(String email) {
    }
}
