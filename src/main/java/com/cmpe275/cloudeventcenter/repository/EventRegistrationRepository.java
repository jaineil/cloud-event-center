package com.cmpe275.cloudeventcenter.repository;

import com.cmpe275.cloudeventcenter.model.Event;
import com.cmpe275.cloudeventcenter.model.EventRegistration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRegistrationRepository extends CrudRepository<EventRegistration, Long> {
    EventRegistration getEventRegistrationByRegistrationId(long registrationId);
}
