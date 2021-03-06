package com.cmpe275.cloudeventcenter.controller;

import com.cmpe275.cloudeventcenter.model.Event;
import com.cmpe275.cloudeventcenter.model.SignupForum;
import com.cmpe275.cloudeventcenter.model.UserInfo;
import com.cmpe275.cloudeventcenter.service.*;
import com.cmpe275.cloudeventcenter.utils.EmailNotifierService;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://18.144.15.109:3000")
@RestController
@RequestMapping("/signupForum")
public class SignupForumController {
    @Autowired private SignupForumService signupForumService;
    @Autowired private UserService userService;
    @Autowired private EventService eventService;

    @Autowired private EventRegistrationService eventRegistrationService;

    @Autowired private VirtualClockService virtualClockService;
@Autowired private EmailNotifierService emailNotifierService;
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

        if (eventService.isSignupForumReadOnly(eventId)) {
            return new ResponseEntity<>("Sign-up forum is read-only now", HttpStatus.NOT_FOUND);
        }

        LocalDateTime currentTime = virtualClockService.getVirtualClock();

        SignupForum message = SignupForum.builder()
                    .userInfo(fetchedUser)
                    .messageText(messageText)
                    .imageUrl(imageUrl)
                    .eventId(eventId)
                .timestamp(currentTime)
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
        List<SignupForum> finalMessageList = new ArrayList<SignupForum>();
        for (SignupForum m: messageList) {
            long messageEventId = m.getEventId();
            Event messageEvent = eventService.getEventById(messageEventId);
            String organizerId = messageEvent.getUserInfo().getUserId();
            if (m.getUserInfo().getUserId().equals(organizerId)) {
                m.setByOrganizer(true);
            } else {
                m.setByOrganizer(false);
            }
            finalMessageList.add(m);
        }
        messageList.clear();
        messageList.addAll(finalMessageList);
        return new ResponseEntity<>(messageList, HttpStatus.OK);
    }
    public void onNewMessageInSignUpForum(SignupForum message){
        long eventId = message.getEventId();
        Event event = eventService.getEventById(eventId);
        String organizerMail = event.getUserInfo().getEmailId();
        String eventTitle = event.getTitle();
        emailNotifierService.notify(organizerMail,
                "CEC Forum Alert",
                "Hi, \n\n A new message has been posted in the participant forum of event "+eventTitle +"\n \n CEC Team");
    }
}
