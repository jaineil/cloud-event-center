package com.cmpe275.cloudeventcenter.service;

import com.cmpe275.cloudeventcenter.model.Event;
import com.cmpe275.cloudeventcenter.repository.EventRepository;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public long insert(Event event) {
        long eventId = eventRepository.save(event).getEventId();
        return eventId;
    }

    public List searchEvents(
            String location,
            Enum eventStatus,
            LocalDateTime startTime,
            LocalDateTime endTime,
            String keyword,
            String organizer
    ) {
        List<Event> allEvents = eventRepository.findAll();
        return allEvents;
    }

    public Event getEventById(
            long eventId
    ) {
        Event event = eventRepository.getEventByEventId(eventId);
        return event;
    }
}
