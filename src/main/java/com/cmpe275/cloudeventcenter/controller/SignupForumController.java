package com.cmpe275.cloudeventcenter.controller;

import com.cmpe275.cloudeventcenter.model.SignupForum;
import com.cmpe275.cloudeventcenter.model.UserInfo;
import com.cmpe275.cloudeventcenter.service.EventService;
import com.cmpe275.cloudeventcenter.service.SignupForumService;
import com.cmpe275.cloudeventcenter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/signupForum")
public class SignupForumController {
    @Autowired private SignupForumService signupForumService;
    @Autowired private UserService userService;
    @Autowired private EventService eventService;

    @PostMapping("/addMessage")
    public ResponseEntity<?> saveMessage(
            @RequestBody Map<?,?> reqBody
    ) {

        String userId = String.valueOf(reqBody.get("userId"));
        UserInfo fetchedUser = userService.getUserInfo(userId);

        String messageText = String.valueOf(reqBody.get("messageText"));
        String imageUrl = String.valueOf(reqBody.get("imageUrl"));
        String eventIdStr = String.valueOf(reqBody.get("eventId"));
        Integer eventIdInt = Integer.parseInt(eventIdStr);
        Long eventId = Long.valueOf(eventIdInt);

        if (!eventService.isSignupForumReadOnly(eventId)) {
            return new ResponseEntity<>("Sign-up forum is read-only now", HttpStatus.NOT_FOUND);
        }

        SignupForum message = SignupForum.builder()
                    .userInfo(fetchedUser)
                    .messageText(messageText)
                    .imageUrl(imageUrl)
                    .eventId(eventId)
                .build();

        long messageId = signupForumService.addMessageToSignupForum(message);

        return new ResponseEntity<>("Inserted message with id: " + messageId, HttpStatus.OK);
    }

    @GetMapping("/{eventIdPathVar}")
    @ResponseBody
    public ResponseEntity<?> getAllMessages(
            @PathVariable String eventIdPathVar
    ) {

        String eventIdStr = String.valueOf(eventIdPathVar);
        Integer eventIdInt = Integer.parseInt(eventIdStr);
        Long eventId = Long.valueOf(eventIdInt);
        List<SignupForum> messageList = signupForumService.getAllMessagesForEvent(eventId);

        return new ResponseEntity<>(messageList, HttpStatus.OK);
    }


}
