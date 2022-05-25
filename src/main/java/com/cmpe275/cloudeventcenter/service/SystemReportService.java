package com.cmpe275.cloudeventcenter.service;

import com.cmpe275.cloudeventcenter.model.Event;
import com.cmpe275.cloudeventcenter.repository.EventRegistrationRepository;
import com.cmpe275.cloudeventcenter.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Map<?,?> getNumberOfEventsAndPaidEvents (LocalDateTime currentTime) {

        Map<String, Object> res = new HashMap<String, Object>();

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

        System.out.println(res.get("numberOfCreatedEvents") + " " + res.get("percentageOfPaidEvents"));
        return res;
    }
}
