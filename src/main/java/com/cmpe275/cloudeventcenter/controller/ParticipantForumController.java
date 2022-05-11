package com.cmpe275.cloudeventcenter.controller;

import com.cmpe275.cloudeventcenter.model.ParticipantForum;
import com.cmpe275.cloudeventcenter.model.SignupForum;
import com.cmpe275.cloudeventcenter.model.UserInfo;
import com.cmpe275.cloudeventcenter.service.ParticipantForumService;
import com.cmpe275.cloudeventcenter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/participantForum")
public class ParticipantForumController {
    @Autowired private ParticipantForumService participantForumService;
    @Autowired private UserService userService;

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

        ParticipantForum message = ParticipantForum.builder()
                .userInfo(fetchedUser)
                .messageText(messageText)
                .imageUrl(imageUrl)
                .eventId(eventId)
                .build();

        long messageId = participantForumService.addMessageToSignupForum(message);

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
        List<ParticipantForum> messageList = participantForumService.getAllMessagesForEvent(eventId);

        return new ResponseEntity<>(messageList, HttpStatus.OK);
    }
}
