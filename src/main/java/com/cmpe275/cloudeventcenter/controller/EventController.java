package com.cmpe275.cloudeventcenter.controller;

import com.cmpe275.cloudeventcenter.model.Event;
import com.cmpe275.cloudeventcenter.service.EventService;
import com.cmpe275.cloudeventcenter.utils.Enum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("event")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping()
    public ResponseEntity<String> testAPI(
            @RequestBody Map<?,?> eventReq
    ) {
        String title = String.valueOf(eventReq.get("title"));
        String description = String.valueOf(eventReq.get("description"));
        LocalDateTime startTime = LocalDateTime.parse((CharSequence) eventReq.get("startTime"));
        LocalDateTime endTime = LocalDateTime.parse((CharSequence) eventReq.get("endTime"));
        LocalDateTime deadline = LocalDateTime.parse((CharSequence) eventReq.get("deadline"));
        int minParticipants = (int) eventReq.get("minParticipants");
        int maxParticipants = (int) eventReq.get("maxParticipants");
        double fee = (double) eventReq.get("fee");
        Enum.AdmissionPolicy admissionPolicy = Enum.AdmissionPolicy.valueOf((String) eventReq.get("admissionPolicy"));

        Event event = Event.builder()
                .title(title)
                .description(description)
                .startTime(startTime)
                .endTime(endTime)
                .deadline(deadline)
                .minParticipants(minParticipants)
                .maxParticipants(maxParticipants)
                .fee(fee)
                .admissionPolicy(admissionPolicy)
                .build();

        long eventId = eventService.insert(event);
        return new ResponseEntity<String>("Successfully created event with id: " + eventId, HttpStatus.CREATED);
    }
}
