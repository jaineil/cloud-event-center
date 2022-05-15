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
import java.util.List;
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
        public ResponseEntity<String> registerEventAPI(
                @RequestBody Map<?,?> eventRegReq
    ) {

        long eventId = (long) (int) eventRegReq.get("eventId");
        String participantId = String.valueOf(eventRegReq.get("participantId"));

        Event event = eventService.getEventById(eventId);
        UserInfo userInfo = userService.getUserInfo(participantId);

        if (participantId.equals(event.getUserInfo().getUserId())) {
            return new ResponseEntity<String>("You cannot register for an event you created yourself", HttpStatus.FORBIDDEN);
        }

        Enum.AccountType accountType = userInfo.getAccountType();
        if (accountType.equals(Enum.AccountType.Organization)) {
            return new ResponseEntity<String>("Organizations are not allowed to register for events", HttpStatus.FORBIDDEN);
        }

        if (!event.getEventStatus().equals(Enum.EventStatus.SignUpOpen)) {{
            return new ResponseEntity<String>("This event is not accepting registrations any more", HttpStatus.FORBIDDEN);
        }}

        int registrationCount = eventRegistrationService.getEventRegistrationCount(eventId);
        if (registrationCount >= event.getMaxParticipants()) {
            return new ResponseEntity<String>("The max registrations have already been completed", HttpStatus.FORBIDDEN);
        }

        if (eventRegistrationService.checkIfAlreadyRegistered(event, userInfo)) {
            return new ResponseEntity<String>("You cannot register for the same event twice", HttpStatus.FORBIDDEN);
        }

        boolean isApproved = false;
        boolean isPaid = false;
        if (event.getAdmissionPolicy().equals(Enum.AdmissionPolicy.FirstComeFirstServe)) {
            isApproved = true;
            isPaid = true;
        }

        EventRegistration eventRegistration = EventRegistration.builder()
                .event(event)
                .userInfo(userInfo)
                .isApproved(isApproved)
                .isPaid(isPaid)
                .build();

        long eventRegistrationId = eventRegistrationService.insert(eventRegistration);
        if (isApproved) {
            return new ResponseEntity<String>("Successfully registered for event" + eventRegistrationId, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<String>("Thanks for your interest. You will be informed as soon as the organizer approves your request" + eventRegistrationId, HttpStatus.CREATED);
        }

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

    @GetMapping("/approvalRequests")
    public ResponseEntity<List> getAllEventsByOrganizer(
            @RequestParam long eventId
    ) {
        List<EventRegistration> approvalRequests = eventRegistrationService.getAllApprovalRequests(eventId);
        return new ResponseEntity<List>(approvalRequests, HttpStatus.OK);
    }
}
