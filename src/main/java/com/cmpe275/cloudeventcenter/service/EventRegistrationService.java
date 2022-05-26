package com.cmpe275.cloudeventcenter.service;

import com.cmpe275.cloudeventcenter.model.Event;
import com.cmpe275.cloudeventcenter.model.EventRegistration;
import com.cmpe275.cloudeventcenter.model.UserInfo;
import com.cmpe275.cloudeventcenter.repository.EventRegistrationRepository;
import com.cmpe275.cloudeventcenter.utils.Enum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventRegistrationService {

    @Autowired
    private EventRegistrationRepository eventRegistrationRepository;

    public long insert(EventRegistration eventRegistration) {
        long eventRegistrationId = eventRegistrationRepository.save(eventRegistration).getRegistrationId();
        return eventRegistrationId;
    }

    public void completePayment(long registrationId) {
        EventRegistration eventRegistration = eventRegistrationRepository.getEventRegistrationByRegistrationId(registrationId);
        eventRegistration.setIsPaid(true);
        eventRegistrationRepository.save(eventRegistration);
    }

    public void approveRegistration(long registrationId) {
        EventRegistration eventRegistration = eventRegistrationRepository.getEventRegistrationByRegistrationId(registrationId);
        eventRegistration.setIsApproved(true);
        eventRegistrationRepository.save(eventRegistration);
    }

    public void declineRegistration(long registrationId) {
        EventRegistration eventRegistration = eventRegistrationRepository.getEventRegistrationByRegistrationId(registrationId);
        eventRegistration.setIsDeclined(true);
        eventRegistrationRepository.save(eventRegistration);
    }

    public int getEventRegistrationCount(long eventId) {
        return eventRegistrationRepository.getEventRegistrationCount(eventId);
    }

    public boolean isUserRegistered(Event event, UserInfo userInfo) {
        try {
            EventRegistration eventRegistration = eventRegistrationRepository.getEventRegistrationByEventAndUserInfo(event, userInfo);
            return (eventRegistration.getIsApproved()) && (eventRegistration.getIsPaid());
        } catch (Exception e) {
            return false;
        }

    }

    public boolean checkIfAlreadyRegistered(Event event, UserInfo userInfo) {
        try {
            EventRegistration eventRegistration = eventRegistrationRepository.getEventRegistrationByEventAndUserInfo(event, userInfo);
            eventRegistration.getEvent();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List getAllApprovalRequests(long eventId) {
        return eventRegistrationRepository.getAllApprovalRequests(eventId);
    }

    public List getApprovedRequests(long eventId) {
        return eventRegistrationRepository.getApprovedRequests(eventId);
    }

}
