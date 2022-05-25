package com.cmpe275.cloudeventcenter.repository;

import com.cmpe275.cloudeventcenter.model.Event;
import com.cmpe275.cloudeventcenter.model.EventRegistration;
import com.cmpe275.cloudeventcenter.model.UserInfo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRegistrationRepository extends CrudRepository<EventRegistration, Long> {
    EventRegistration getEventRegistrationByRegistrationId(long registrationId);

    EventRegistration getEventRegistrationByEventAndUserInfo(Event event, UserInfo userInfo);

    @Query(value = "UPDATE player SET team_id = NULL WHERE team_id = ?1", nativeQuery = true)
    void updateAllPlayersOfTeam(long teamId);

    @Query(value = "SELECT COUNT(DISTINCT participant_id) FROM event_registration WHERE event_id = ?1 AND is_approved IS TRUE", nativeQuery = true)
    int getEventRegistrationCount(long eventId);

    @Query(value = "SELECT * FROM event_registration WHERE event_id = ?1 AND is_approved IS NOT TRUE", nativeQuery = true)
    List<EventRegistration> getAllApprovalRequests(long eventId);

    @Query(value = "SELECT * FROM event_registration WHERE event_id = ?1 AND is_approved IS TRUE", nativeQuery = true)
    List<EventRegistration> getApprovedRequests(long eventId);
}
