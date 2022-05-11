package com.cmpe275.cloudeventcenter.controller;

import com.cmpe275.cloudeventcenter.model.Address;
import com.cmpe275.cloudeventcenter.model.Event;
import com.cmpe275.cloudeventcenter.service.AddressService;
import com.cmpe275.cloudeventcenter.service.EventService;
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

                Event event = Event.builder()
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
                                .build();

                long eventId = eventService.insert(event);
                return new ResponseEntity<String>("Successfully created event with id: " + eventId, HttpStatus.CREATED);
        }

        @GetMapping("/all")
        public ResponseEntity<List> searchForEventsAPI() {
                List<Event> allEvents = eventService.getAllEvents();
                return new ResponseEntity<List>(allEvents, HttpStatus.OK);
        }

        @GetMapping("/search")
        public ResponseEntity<List> searchForEventsAPI(
                        @RequestParam(required = false) String location,
                        @RequestParam(name = "eventStatus", required = false) String eventStatusString,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
                        @RequestParam(required = false) String keyword,
                        @RequestParam(required = false) String organizer) {

                Enum.EventStatus eventStatus = Enum.EventStatus.valueOf(eventStatusString);

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
}
