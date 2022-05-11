package com.cmpe275.cloudeventcenter.service;

import com.cmpe275.cloudeventcenter.model.Event;
import com.cmpe275.cloudeventcenter.model.EventRegistration;
import com.cmpe275.cloudeventcenter.model.UserInfo;
import com.cmpe275.cloudeventcenter.repository.EventRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public boolean isUserRegistered(Event event, UserInfo userInfo) {
        EventRegistration eventRegistration = eventRegistrationRepository.getEventRegistrationByEventAndUserInfo(event, userInfo);
        if (eventRegistration.getIsApproved() == false) {
            return false;
        } else {
            return true;
        }
    }
}
