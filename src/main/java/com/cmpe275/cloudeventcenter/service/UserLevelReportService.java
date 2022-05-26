package com.cmpe275.cloudeventcenter.service;

import com.cmpe275.cloudeventcenter.repository.EventRegistrationRepository;
import com.cmpe275.cloudeventcenter.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserLevelReportService {
    @Autowired
    private EventRegistrationRepository eventRegistrationRepository;

    @Autowired
    private EventRepository eventRepository;

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

    public Map<String, Object> getNumberOfEventsCreatedForOrganizer(LocalDateTime currentTime, String userId) {
        Map<String, Object> res = new HashMap<>();
        LocalDateTime start = currentTime.minusDays(90);

        int numberOfEventsCreated = 0;
        int numberOfPaidEventsCreated = 0;
        float percentageOfPaidEvents = 0;

        try {
            numberOfEventsCreated = eventRepository.getNumberOfEventsCreatedForOrganizer(userId, start);
            numberOfPaidEventsCreated = eventRepository.getNumberOfPaidEventsCreatedForOrganizer(userId, start);
            if (numberOfEventsCreated != 0 && numberOfPaidEventsCreated != 0) {
                percentageOfPaidEvents = ((numberOfPaidEventsCreated / numberOfEventsCreated) * 100);
            }
        } catch (Exception e) {
            System.err.println(e);
        }

        res.put("numberOfEventsCreatedForOrganizer", numberOfEventsCreated);
        res.put("percentageOfPaidEventsCreatedForOrganizer", percentageOfPaidEvents);
        return res;
    }

    public Map<String, Object> getNumberOfCancelledEventsForOrganizer(LocalDateTime currentTime, String userId) {
        Map<String, Object> res = new HashMap<>();
        LocalDateTime start = currentTime.minusDays(90);

        int numberOfCancelledEvents = 0;

        try {
            numberOfCancelledEvents = eventRepository.getNumberOfCancelledEventsForOrganizer(userId, start);
        } catch (Exception e) {
            System.err.println(e);
        }

        res.put("numberOfCancelledEventsForOrganizer", numberOfCancelledEvents);
        return res;
    }

    public Map<String, Object> getRatioOfParticipantRequestsToMinimumRequiredParticipants(LocalDateTime currentTime, String userId) {
        Map<String, Object> res = new HashMap<>();
        LocalDateTime start = currentTime.minusDays(90);

        float ratio = 0;

        try {
            ratio = eventRepository.getRatioOfRegisteredToMinimumParticipantsForCancelledEvents(userId, start);
            System.out.println("Ratio of registered participants to minimum participants for cancelled events for organizer => " + ratio);
        } catch (Exception e) {
            System.err.println(e);
        }

        res.put("ratioOfRegisteredToMinimumParticipantsForCancelledEvents", ratio);
        return res;
    }

    public Map<String, Object> getAverageNumberOfParticipantsAmongFinishedEventsForOrganization(LocalDateTime currentTime, String userId) {
        Map<String, Object> res = new HashMap<>();
        LocalDateTime start = currentTime.minusDays(90);

        BigDecimal average = BigDecimal.valueOf(0);
        int numberOfFinishedEvents = 0;

        try {
            average = eventRepository.getAverageNumberOfParticipantsForFinishedEventsForOrganizer(userId, start);
            numberOfFinishedEvents = eventRepository.getNumberOfFinishedEventsForOrganizer(userId, start);
            System.out.println("Average number of participants across finished events for organization => " + average);
        } catch (Exception e) {
            System.err.println(e);
        }
        res.put("numberOfFinishedEventsForOrganization", numberOfFinishedEvents);
        res.put("averageNumberOfParticipantsAmongFinishedEventsForOrganization", average);
        return res;
    }

    public Map<String, Object> getRevenueOfPaidAndFinishedEventsForOrganizer(LocalDateTime currentTime, String userId) {
        Map<String, Object> res = new HashMap<>();
        LocalDateTime start = currentTime.minusDays(90);

        int totalFinishedPaidAndEvents = 0;
        int revenueOfPaidAndFinishedEvents = 0;

        try {
            totalFinishedPaidAndEvents = eventRepository.getNumberOfPaidAndFinishedEventsForOrganizer(userId, start);
            revenueOfPaidAndFinishedEvents = eventRepository.getRevenueOfPaidAndFinishedEventsForOrganizer(userId, start);
            System.out.println("Total revenue => " + revenueOfPaidAndFinishedEvents);
        } catch (Exception e) {
            System.err.println(e);
        }

        res.put("totalFinishedPaidAndEventsForOrganizer", totalFinishedPaidAndEvents);
        res.put("revenueOfPaidAndFinishedEventsForOrganizer", revenueOfPaidAndFinishedEvents);

        return res;
    }

}
