package com.cmpe275.cloudeventcenter.service;

import com.cmpe275.cloudeventcenter.repository.EventRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserLevelReportService {
    @Autowired
    private EventRegistrationRepository eventRegistrationRepository;

    public Map<String, Object> getNumberOfEventsForParticipant (LocalDateTime currentTime, String userId) {
        Map<String, Object> res = new HashMap<>();
        LocalDateTime start = currentTime.minusDays(90);

        int numberOfEventsForParticipant = 0;

        try {
            numberOfEventsForParticipant = eventRegistrationRepository.getNumberOfRegistrationsForUser(userId, start);
        } catch (Exception e) {
            System.err.println(e);
        }

        res.put("numberOfEventsForParticipant", numberOfEventsForParticipant);
        return res;
    }

    public Map<String, Object> getNumberOfRejectsForParticipant (LocalDateTime currentTime, String userId) {
        Map<String, Object> res = new HashMap<>();
        LocalDateTime start = currentTime.minusDays(90);

        int numberOfRejects = 0;

        try {
            numberOfRejects = eventRegistrationRepository.getNumberOfRejectsForUser(userId, start);
        } catch (Exception e) {
            System.err.println(e);
        }

        res.put("numberOfRejectsForParticipant", numberOfRejects);
        return res;
    }

    public Map<String, Object> getNumberOfAcceptsForParticipant (LocalDateTime currentTime, String userId) {
        Map<String, Object> res = new HashMap<>();
        LocalDateTime start = currentTime.minusDays(90);

        int numberOfAccepts = 0;

        try {
            numberOfAccepts = eventRegistrationRepository.getNumberOfAcceptsForUser(userId, start);
        } catch (Exception e) {
            System.err.println(e);
        }

        res.put("numberOfAcceptsForParticipant", numberOfAccepts);
        return res;
    }

    public Map<String, Object> getNumberOfFinishedEventsForParticipant(LocalDateTime currentTime, String userId) {
        Map<String, Object> res = new HashMap<>();
        LocalDateTime start = currentTime.minusDays(90);

        int numberOfFinishedEvents = 0;

        try {
            numberOfFinishedEvents = eventRegistrationRepository.getNumberOfFinishedEventsForUser(userId, start);
        } catch (Exception e) {
            System.err.println(e);
        }

        res.put("numberOfFinishedEventsForParticipant", numberOfFinishedEvents);
        return res;
    }
}
