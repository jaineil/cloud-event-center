package com.cmpe275.cloudeventcenter.service;

import com.cmpe275.cloudeventcenter.model.Event;
import com.cmpe275.cloudeventcenter.repository.EventRepository;
import com.cmpe275.cloudeventcenter.utils.Enum;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public long insert(Event event) {
        long eventId = eventRepository.save(event).getEventId();
        return eventId;
    }

    public List getAllEvents() {
        List<Event> allEvents = eventRepository.findAll();
        return allEvents;
    }

    public List searchEvents(
            String location,
            String eventStatus,
            LocalDateTime startTime,
            LocalDateTime endTime,
            String keyword,
            String organizer
    ) {
        List<Event> allEvents = eventRepository.findAll();
        List<Event> filteredEvents = new ArrayList<Event>();

        System.out.println("Number of total events: " + allEvents.size());

        //location: empty: match all, otherwise match city of event
        if ((location != null) && (!location.isEmpty())) {
            for (Event e: allEvents) {
                if (e.getAddress().getCity().equalsIgnoreCase(location)) {
                    filteredEvents.add(e);
                }
            }
            allEvents.clear();
            allEvents.addAll(filteredEvents);
            filteredEvents.clear();
        }
        System.out.println("Number of events after filtering by location: " + allEvents.size());

        List<Enum.EventStatus> requiredStatuses = new ArrayList<>();
        if ((eventStatus == null) || (eventStatus.isEmpty()) || (eventStatus.equalsIgnoreCase("active")) ) {
            requiredStatuses.add(Enum.EventStatus.SignUpOpen);
            requiredStatuses.add(Enum.EventStatus.SignUpClosed);
            requiredStatuses.add(Enum.EventStatus.Ongoing);
        } else if (eventStatus.equalsIgnoreCase("openForRegistration")) {
            requiredStatuses.add(Enum.EventStatus.SignUpOpen);
            requiredStatuses.add(Enum.EventStatus.SignUpClosed);
        } else if (eventStatus.equalsIgnoreCase("all")) {
            requiredStatuses.add(Enum.EventStatus.SignUpOpen);
            requiredStatuses.add(Enum.EventStatus.SignUpClosed);
            requiredStatuses.add(Enum.EventStatus.Ongoing);
            requiredStatuses.add(Enum.EventStatus.Cancelled);
            requiredStatuses.add(Enum.EventStatus.Finished);
        } else {
            return Collections.emptyList();
        }

        for (Event e: allEvents) {
            // status: active / openForRegistration / all
            if (requiredStatuses.contains(e.getEventStatus())) {
                filteredEvents.add(e);
            }
        }
        allEvents.clear();
        allEvents.addAll(filteredEvents);
        filteredEvents.clear();
        System.out.println("Number of events after filtering by status: " + allEvents.size());

        if (startTime == null) {
            startTime = LocalDateTime.now();
        }
        if (endTime == null) {
            endTime = LocalDateTime.parse("2100-12-12T00:00");
        }
        for (Event e: allEvents) {
            if (e.getStartTime().isAfter(startTime) && (e.getEndTime().isBefore(endTime))) {
                filteredEvents.add(e);
            }
        }

        allEvents.clear();
        allEvents.addAll(filteredEvents);
        filteredEvents.clear();
        System.out.println("Number of events after filtering by startTime and endTime: " + allEvents.size());

        if ((keyword != null) && (!keyword.isEmpty())) {
            for (Event e : allEvents) {
                if (e.getTitle().toUpperCase().contains(keyword.toUpperCase())) {
                    filteredEvents.add(e);
                } else if (e.getDescription().toUpperCase().contains(keyword.toUpperCase())) {
                    filteredEvents.add(e);
                }
            }
            allEvents.clear();
            allEvents.addAll(filteredEvents);
            filteredEvents.clear();
        }
        System.out.println("Number of events after filtering by keyword: " + allEvents.size());

        if ((organizer != null) && (!organizer.isEmpty())) {
            for (Event e : allEvents) {
                if (e.getUserInfo().getScreenName().toUpperCase().contains(organizer.toUpperCase())) {
                    filteredEvents.add(e);
                }
            }
            allEvents.clear();
            allEvents.addAll(filteredEvents);
            filteredEvents.clear();
        }
        System.out.println("Number of events after filtering by organizer: " + allEvents.size());
        return allEvents;
    }

    public Event getEventById(
            long eventId
    ) {
        Event event = eventRepository.getEventByEventId(eventId);
        return event;
    }
}
