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
import com.cmpe275.cloudeventcenter.service.VirtualClockService;
import com.cmpe275.cloudeventcenter.utils.EmailNotifierService;
import com.cmpe275.cloudeventcenter.utils.Enum;
import org.apache.tomcat.jni.Local;
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

    @Autowired
    private EmailNotifierService emailNotifierService;

    @Autowired
    private EventRegistrationRepository eventRegistrationRepository;

    @Autowired
    private VirtualClockService virtualClockService;

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
        boolean isDeclined = false;

        if (event.getFee() == 0) {
            isPaid = true;
        }

        LocalDateTime currentTime = virtualClockService.getVirtualClock();
        System.out.println("The current virtual time is: " + currentTime);

        EventRegistration eventRegistration = EventRegistration.builder()
                .event(event)
                .userInfo(userInfo)
                .isApproved(isApproved)
                .isPaid(isPaid)
                .isDeclined(isDeclined)
                .signupTime(currentTime)
                .approveOrRejectTime(currentTime)
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
        LocalDateTime currentTime = virtualClockService.getVirtualClock();
        eventRegistrationService.approveRegistration(registrationId, currentTime);
        return new ResponseEntity<String>("Successfully approved registration with id: " + registrationId, HttpStatus.CREATED);
    }

    @PutMapping("/decline")
    public ResponseEntity<String> declineRegistrationAPI(
            @RequestBody Map<?,?> eventRegReq
    ) {

        long registrationId = (long) (int) eventRegReq.get("registrationId");
        LocalDateTime currentTime = virtualClockService.getVirtualClock();
        eventRegistrationService.declineRegistration(registrationId, currentTime);
        return new ResponseEntity<String>("Successfully declined registration with id: " + registrationId, HttpStatus.CREATED);
    }

    @GetMapping("/approvalRequests")
    public ResponseEntity<List> getApprovalRequestsForEvent(
            @RequestParam long eventId
    ) {
        List<EventRegistration> approvalRequests = eventRegistrationService.getAllApprovalRequests(eventId);
        return new ResponseEntity<List>(approvalRequests, HttpStatus.OK);
    }

    @GetMapping("/approvedRequests")
    public ResponseEntity<List> getApprovedRequests(
            @RequestParam long eventId
    ) {
        List<EventRegistration> approvedRequests = eventRegistrationService.getApprovedRequests(eventId);
        return new ResponseEntity<List>(approvedRequests, HttpStatus.OK);
    }



    public void onEventSignupMail(EventRegistration eventRegistration){
        String to=eventRegistration.getUserInfo().getEmailId();
        String eventTitle = eventRegistration.getEvent().getTitle();
        emailNotifierService.notify(to,
                "CEC Event Registration",
                eventRegistration.getEvent().getAdmissionPolicy().equals("FirstComeFirstServe")?
                        "Hi, \n\n You have successfully registered to the event "+eventTitle+"\n \n CEC Team"
                        :"Hi, \n\n Your signup request is pending for event "+eventTitle+
                "\n \n CEC Team");
    }

    public void onEventRegistrationApproval(long registrationId){
        EventRegistration eventRegistration = eventRegistrationRepository.getEventRegistrationByRegistrationId(registrationId);
        Event event=eventRegistration.getEvent();
        String to=eventRegistration.getUserInfo().getEmailId();
        String eventTitle = event.getTitle();
        emailNotifierService.notify(to,
                "CEC Event Registration",
                "Hi, \n\n Your Signup Request for the event "+eventTitle+" has been approved!" +"\n \n CEC Team");
    }

    public void onEventRegistrationDecline(long registrationId){
        EventRegistration eventRegistration = eventRegistrationRepository.getEventRegistrationByRegistrationId(registrationId);
        Event event=eventRegistration.getEvent();
        String to=eventRegistration.getUserInfo().getEmailId();
        String eventTitle = event.getTitle();
        emailNotifierService.notify(to,
                "CEC Event Registration",
                "Hi, \n\n Your Signup Request for the event "+eventTitle+" has been declined!" +"\n \n CEC Team");
    }

    @GetMapping("/testXYZ")
    public ResponseEntity<List> getTotalEventSignups(
            @RequestParam long eventId
    ) {
        List<String> emailIds = eventRegistrationService.getAllSignupsForEvent(eventId);
        return new ResponseEntity<List>(emailIds, HttpStatus.OK);
    }
}
