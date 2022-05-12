package com.cmpe275.cloudeventcenter.controller;

import com.cmpe275.cloudeventcenter.model.Event;
import com.cmpe275.cloudeventcenter.model.ParticipantForum;
import com.cmpe275.cloudeventcenter.model.UserInfo;
import com.cmpe275.cloudeventcenter.service.EventRegistrationService;
import com.cmpe275.cloudeventcenter.service.EventService;
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
    @Autowired private EventService eventService;
    @Autowired private EventRegistrationService eventRegistrationService;

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

        if (eventService.isParticipantForumReadOnly(eventId)) {
            return new ResponseEntity<>("Participant forum is read-only now", HttpStatus.NOT_FOUND);
        }

        Event event = eventService.getEventById(eventId);
        UserInfo userInfo = userService.getUserInfo(userId);
        if (!eventRegistrationService.isUserRegistered(event, userInfo)) {
            return new ResponseEntity<>("User is not a participant of the event", HttpStatus.NOT_FOUND);
        }

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

    @GetMapping("/checkAccess")
    @ResponseBody
    public ResponseEntity<Boolean> getAllMessages(
            @RequestParam String userId,
            @RequestParam long eventId
    ) {
        Event event = eventService.getEventById(eventId);
        UserInfo userInfo = userService.getUserInfo(userId);
        try {
            if ((userId.equals(event.getUserInfo().getUserId())) || eventRegistrationService.isUserRegistered(event, userInfo)) {
                return new ResponseEntity<Boolean>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<Boolean>(false, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<Boolean>(false, HttpStatus.OK);
        }
    }
}
