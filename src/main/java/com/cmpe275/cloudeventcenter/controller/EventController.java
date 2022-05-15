package com.cmpe275.cloudeventcenter.controller;

import com.cmpe275.cloudeventcenter.model.Address;
import com.cmpe275.cloudeventcenter.model.Event;
import com.cmpe275.cloudeventcenter.model.UserInfo;
import com.cmpe275.cloudeventcenter.repository.EventRegistrationRepository;
import com.cmpe275.cloudeventcenter.service.AddressService;
import com.cmpe275.cloudeventcenter.service.EventRegistrationService;
import com.cmpe275.cloudeventcenter.service.EventService;
import com.cmpe275.cloudeventcenter.service.UserService;
import com.cmpe275.cloudeventcenter.utils.Enum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.coyote.Response;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/event")
public class EventController {

        @Autowired
        private EventService eventService;

        @Autowired
        private AddressService addressService;

        @Autowired
        private UserService userService;

        @Autowired
        private EventRegistrationService eventRegistrationService;

        @PostMapping()
        public ResponseEntity<String> createEventAPI(
                        @RequestBody Map<?, ?> eventReq) {

                String street = String.valueOf(eventReq.get("street"));
                String apartment = String.valueOf(eventReq.get("apartment"));
                String city = String.valueOf(eventReq.get("city"));
                String state = String.valueOf(eventReq.get("state"));
                String zipcode = String.valueOf(eventReq.get("zipcode"));

                Address address = Address.builder()
                                .street(street)
                                .apartment(apartment)
                                .city(city)
                                .state(state)
                                .zipcode(zipcode)
                                .build();

                addressService.insert(address);

                String organizerId = String.valueOf(eventReq.get("organizerId"));
                UserInfo userInfo = userService.getUserInfo(organizerId);

                String title = String.valueOf(eventReq.get("title"));
                String description = String.valueOf(eventReq.get("description"));
                LocalDateTime startTime = LocalDateTime.parse((CharSequence) eventReq.get("startTime"));
                LocalDateTime endTime = LocalDateTime.parse((CharSequence) eventReq.get("endTime"));
                LocalDateTime deadline = LocalDateTime.parse((CharSequence) eventReq.get("deadline"));
                Integer minParticipants = Integer.parseInt(String.valueOf(eventReq.get("minParticipants")));
                Integer maxParticipants = Integer.parseInt(String.valueOf(eventReq.get("maxParticipants")));
                Double fee = Double.parseDouble(String.valueOf(eventReq.get("fee")));
                String imageUrl = String.valueOf(eventReq.get("imageUrl"));
                Enum.AdmissionPolicy admissionPolicy = Enum.AdmissionPolicy.valueOf((String) eventReq.get("admissionPolicy"));

                Enum.EventStatus eventStatus = Enum.EventStatus.valueOf("SignUpOpen");

                LocalDateTime currentTime = LocalDateTime.now();
                if ((startTime.isBefore(currentTime)) || (endTime.isBefore(currentTime))) {
                        return new ResponseEntity<String>("Start and end time must be in the future", HttpStatus.BAD_REQUEST);
                } else if (startTime.isAfter(endTime)) {
                        return new ResponseEntity<String>("Start time must be before end time", HttpStatus.BAD_REQUEST);
                } else if (deadline.isAfter(startTime)) {
                        return new ResponseEntity<String>("Deadline must not be after the start time", HttpStatus.BAD_REQUEST);
                } else if ((minParticipants < 0) || (maxParticipants < 0)) {
                        return new ResponseEntity<String>("Minimum and maximum participants must not be negative", HttpStatus.BAD_REQUEST);
                } else if (maxParticipants < minParticipants) {
                        return new ResponseEntity<String>("Minimum participants cannot be greater than maximum participants", HttpStatus.BAD_REQUEST);
                } else if ((userInfo.getAccountType().equals(Enum.AccountType.Person))) {
                        if (fee > 0) {
                                return new ResponseEntity<String>("Only events created by an organization can require a fee", HttpStatus.BAD_REQUEST);
                        }
                }

                Event event = Event.builder()
                                .userInfo(userInfo)
                                .title(title)
                                .description(description)
                                .startTime(startTime)
                                .endTime(endTime)
                                .deadline(deadline)
                                .address(address)
                                .minParticipants(minParticipants)
                                .maxParticipants(maxParticipants)
                                .fee(fee)
                                .imageUrl(imageUrl)
                                .admissionPolicy(admissionPolicy)
                                .eventStatus(eventStatus)
                                .isSignUpForumReadOnly(false)
                                .isParticipantForumReadOnly(false)
                                .build();

                long eventId = eventService.insert(event);
                return new ResponseEntity<String>("Successfully created event with id: " + eventId, HttpStatus.CREATED);
        }

        @GetMapping("/all")
        public ResponseEntity<List> getAllEventsAPI() {
                List<Event> allEvents = eventService.getAllEvents();
                return new ResponseEntity<List>(allEvents, HttpStatus.OK);
        }

        @GetMapping("/search")
        public ResponseEntity<?> searchForEventsAPI(
                        @RequestParam(required = false) String location,
                        @RequestParam(name = "eventStatus", required = false) String eventStatus,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
                        @RequestParam(required = false) String keyword,
                        @RequestParam(required = false) String organizer) {

//                System.out.println(location);
//                System.out.println(eventStatus);
//                System.out.println(startTime);
//                System.out.println(endTime);
//                System.out.println(keyword);
//                System.out.println(organizer);
//
                List<Event> allEvents = eventService.searchEvents(
                                location,
                                eventStatus,
                                startTime,
                                endTime,
                                keyword,
                                organizer);

                List<Event> returnList = new ArrayList<Event>();
                for (Event e: allEvents) {
                        e.setRegistrationCount(eventRegistrationService.getEventRegistrationCount(e.getEventId()));
                        returnList.add(e);
                }
                allEvents.clear();
                allEvents.addAll(returnList);
                return new ResponseEntity<List>(allEvents, HttpStatus.OK);
        }

        @GetMapping("/{eventId}")
        public ResponseEntity<Event> getEventByIdAPI(
                        @PathVariable("eventId") long eventId) {
                Event event = eventService.getEventById(eventId);
                event.setRegistrationCount(eventRegistrationService.getEventRegistrationCount(event.getEventId()));
                return new ResponseEntity<Event>(event, HttpStatus.OK);
        }
}
