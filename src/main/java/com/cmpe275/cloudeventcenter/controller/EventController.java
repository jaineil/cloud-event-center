package com.cmpe275.cloudeventcenter.controller;

import com.cmpe275.cloudeventcenter.model.Address;
import com.cmpe275.cloudeventcenter.model.Event;
import com.cmpe275.cloudeventcenter.model.UserInfo;
import com.cmpe275.cloudeventcenter.service.AddressService;
import com.cmpe275.cloudeventcenter.service.EventService;
import com.cmpe275.cloudeventcenter.service.UserService;
import com.cmpe275.cloudeventcenter.utils.Enum;
import org.apache.coyote.Response;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
                int minParticipants = (int) eventReq.get("minParticipants");
                int maxParticipants = (int) eventReq.get("maxParticipants");
                double fee = (double) eventReq.get("fee");
                String imageUrl = String.valueOf(eventReq.get("imageUrl"));
                Enum.AdmissionPolicy admissionPolicy = Enum.AdmissionPolicy
                                .valueOf((String) eventReq.get("admissionPolicy"));
                Enum.EventStatus eventStatus = Enum.EventStatus.valueOf("SignUpOpen");

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

                System.out.println(location);
                System.out.println(eventStatus);
                System.out.println(startTime);
                System.out.println(endTime);
                System.out.println(keyword);
                System.out.println(organizer);
//
                List<Event> allEvents = eventService.searchEvents(
                                location,
                                eventStatus,
                                startTime,
                                endTime,
                                keyword,
                                organizer);
                return new ResponseEntity<List>(allEvents, HttpStatus.OK);
        }

        @GetMapping("/{eventId}")
        public ResponseEntity<Event> getEventByIdAPI(
                        @PathVariable("eventId") long eventId) {
                Event event = eventService.getEventById(eventId);
                return new ResponseEntity<Event>(event, HttpStatus.OK);
        }

//        @PutMapping("/signup-read-only")
//        public ResponseEntity<String> signupFormReadOnlyAPI(
//                @RequestBody Map<?,?> eventRegReq
//        ) {
//                long eventId = (long) (int) eventRegReq.get("eventId");
//                eventService.makeSignupFormReadOnly(eventId);
//                return new ResponseEntity<String>("Successfully approved registration with id: " + eventId, HttpStatus.CREATED);
//        }
}
