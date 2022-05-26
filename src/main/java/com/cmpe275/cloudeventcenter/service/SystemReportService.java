package com.cmpe275.cloudeventcenter.service;

import com.cmpe275.cloudeventcenter.model.Event;
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

    public Map<String,Object> getNumberOfEventsAndPaidEvents (LocalDateTime currentTime) {

        Map<String, Object> res = new HashMap<>();

        LocalDateTime start = currentTime.minusDays(90);

        List<Event> allEvents = eventRepository.findAllByCreationTimeAfter(start);
        List<Event> allPaidEvents = eventRepository.findAllByCreationTimeAfterAndFeeGreaterThan(start, 0.0);

        int numberOfEvents = allEvents.size();
        int numberOfAllPaidEvents = allPaidEvents.size();
        if (numberOfEvents == 0) {
            res.put("numberOfCreatedEvents", 0);
            res.put("percentageOfPaidEvents", 0.0);
        } else if (numberOfAllPaidEvents == 0) {
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

            ratio = eventRepository.getEventParticipantsToMinRequirementsRatio(Enum.EventStatus.Cancelled.name(), start);

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

    public Map<String, Object> getNumberOfFinishedEvents (LocalDateTime currentTime) {
        Map<String, Object> res = new HashMap<>();
        LocalDateTime start = currentTime.minusDays(90);
        int finishedEventsCount = 0;
        BigDecimal avg = BigDecimal.valueOf(0);

        try {
            finishedEventsCount = eventRepository.getNumberOfFinishedEvents(Enum.EventStatus.Finished.name(), start);
            System.out.println("Count of total finished events => " + finishedEventsCount);
        } catch (Exception e) {
            System.err.println(e);
        }

        try {
            avg = eventRepository.getAverageEventParticipantsInFinishedEvents(Enum.EventStatus.Finished.name(), start);
            System.out.println("Computed average of total event participants to total events for finished events => " + avg);
            if (avg == null) {
                avg = BigDecimal.valueOf(0);
            }
        } catch (Exception e) {
            System.err.println(e);
        }

        res.put("numberOfFinishedEvents", finishedEventsCount);
        res.put("avgTotalParticipantsForFinishedEvents", avg);

        return res;
    }
}
