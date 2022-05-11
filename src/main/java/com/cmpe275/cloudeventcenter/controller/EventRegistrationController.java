package com.cmpe275.cloudeventcenter.controller;

import com.cmpe275.cloudeventcenter.model.Address;
import com.cmpe275.cloudeventcenter.model.Event;
import com.cmpe275.cloudeventcenter.model.EventRegistration;
import com.cmpe275.cloudeventcenter.model.UserInfo;
import com.cmpe275.cloudeventcenter.repository.EventRegistrationRepository;
import com.cmpe275.cloudeventcenter.repository.EventRepository;
import com.cmpe275.cloudeventcenter.service.EventRegistrationService;
import com.cmpe275.cloudeventcenter.service.EventService;
import com.cmpe275.cloudeventcenter.service.UserService;
import com.cmpe275.cloudeventcenter.utils.Enum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/eventReg")
public class EventRegistrationController {
    @Autowired
    private EventRegistrationService eventRegistrationService;

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @PostMapping()
    public ResponseEntity<String> createEventAPI(
            @RequestBody Map<?,?> eventRegReq
    ) {

        long eventId = (long) (int) eventRegReq.get("eventId");
        String participantId = String.valueOf(eventRegReq.get("participantId"));
        boolean isApproved = (boolean) eventRegReq.get("isApproved");
        boolean isPaid = (boolean) eventRegReq.get("isPaid");

        Event event = eventService.getEventById(eventId);
        UserInfo userInfo = userService.getUserInfo(participantId);

        EventRegistration eventRegistration = EventRegistration.builder()
                .event(event)
                .userInfo(userInfo)
                .isApproved(isApproved)
                .isPaid(isPaid)
                .build();

        long eventRegistrationId = eventRegistrationService.insert(eventRegistration);
        return new ResponseEntity<String>("Successfully registered for event with id: " + eventRegistrationId, HttpStatus.CREATED);
    }

    @PutMapping("/pay")
    public ResponseEntity<String> completePaymentAPI(
            @RequestBody Map<?,?> eventRegReq
    ) {

        long registrationId = (long) (int) eventRegReq.get("registrationId");
        eventRegistrationService.completePayment(registrationId);
        return new ResponseEntity<String>("Successfully paid for registration with id: " + registrationId, HttpStatus.CREATED);
    }

    @PutMapping("/approve")
    public ResponseEntity<String> approveRegistrationAPI(
            @RequestBody Map<?,?> eventRegReq
    ) {

        long registrationId = (long) (int) eventRegReq.get("registrationId");
        eventRegistrationService.approveRegistration(registrationId);
        return new ResponseEntity<String>("Successfully approved registration with id: " + registrationId, HttpStatus.CREATED);
    }
}
