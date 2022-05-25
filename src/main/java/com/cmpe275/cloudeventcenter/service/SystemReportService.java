package com.cmpe275.cloudeventcenter.service;

import com.cmpe275.cloudeventcenter.model.Event;
import com.cmpe275.cloudeventcenter.repository.EventRegistrationRepository;
import com.cmpe275.cloudeventcenter.repository.EventRepository;
import com.cmpe275.cloudeventcenter.utils.Enum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SystemReportService {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventRegistrationRepository eventRegistrationRepository;

    public Map<String,Object> getNumberOfEventsAndPaidEvents (LocalDateTime currentTime) {

        Map<String, Object> res = new HashMap<>();

        LocalDateTime start = currentTime.minusDays(90);

        List<Event> allEvents = eventRepository.findAllByStartTimeAfter(start);
        List<Event> allPaidEvents = eventRepository.findAllByStartTimeAfterAndFeeGreaterThan(start, 0.0);

        int numberOfEvents = allEvents.size();
        int numberOfAllPaidEvents = allPaidEvents.size();
        if (numberOfAllPaidEvents == 0) {
            res.put("numberOfCreatedEvents", numberOfEvents);
            res.put("percentageOfPaidEvents", 0.0);
        } else {
            float percentage = (numberOfAllPaidEvents / numberOfEvents) * 100;
            res.put("numberOfCreatedEvents", numberOfEvents);
            res.put("percentageOfPaidEvents", percentage);
        }

        System.out.println("Total number of events created => " + res.get("numberOfCreatedEvents") + " & % of those events paid => " + res.get("percentageOfPaidEvents"));
        return res;
    }

    public Map<String,Object> getNumberOfCanceledEvents (LocalDateTime currentTime) {
        Map<String, Object> res = new HashMap<>();
        LocalDateTime start = currentTime.minusDays(90);
        int cancelledEventsCount = 0;
        BigDecimal ratio = BigDecimal.valueOf(0);

        try {
            cancelledEventsCount = eventRepository.getNumberOfCancelledEvents(Enum.EventStatus.Cancelled.name(), start);
            System.out.println("Count of total cancelled events => " + cancelledEventsCount);
        } catch (Exception e) {
            System.err.println(e);
        }

        try {
            Map<String, BigDecimal> eventParticipantsToMinRequirementsRatio = eventRepository.getEventParticipantsToMinRequirementsRatio(Enum.EventStatus.Cancelled.name(), start);
            ratio = eventParticipantsToMinRequirementsRatio.get("registered_to_min_participants");
            if (ratio == null) {
                ratio = BigDecimal.valueOf(0);
            }
            System.out.println("Computed ratio of total event participants to total minimum participants required for cancelled events => " + ratio);
        } catch (Exception e) {
            System.err.println(e);
        }

        res.put("numberOfCancelledEvents", cancelledEventsCount);
        res.put("eventParticipantsToMinRequirementsRatio", ratio);
        return res;
    }
}
