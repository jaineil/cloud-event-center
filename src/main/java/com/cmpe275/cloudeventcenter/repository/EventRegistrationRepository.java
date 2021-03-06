package com.cmpe275.cloudeventcenter.repository;

import com.cmpe275.cloudeventcenter.model.Event;
import com.cmpe275.cloudeventcenter.model.EventRegistration;
import com.cmpe275.cloudeventcenter.model.UserInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRegistrationRepository extends CrudRepository<EventRegistration, Long> {
    EventRegistration getEventRegistrationByRegistrationId(long registrationId);

    EventRegistration getEventRegistrationByEventAndUserInfo(Event event, UserInfo userInfo);

    @Query(value = "UPDATE player SET team_id = NULL WHERE team_id = ?1", nativeQuery = true)
    void updateAllPlayersOfTeam(long teamId);

    @Query(value = "SELECT COUNT(DISTINCT participant_id) FROM event_registration WHERE event_id = ?1 AND is_approved IS TRUE AND is_paid IS TRUE", nativeQuery = true)
    int getEventRegistrationCount(long eventId);

    @Query(value = "SELECT * FROM event_registration WHERE event_id = ?1 AND is_approved IS NOT TRUE AND is_declined IS NOT TRUE", nativeQuery = true)
    List<EventRegistration> getAllApprovalRequests(long eventId);

    @Query(value = "select count(event_id) from event_registration where participant_id = ?1 and signup_time >= ?2", nativeQuery = true)
    int getNumberOfRegistrationsForUser(String userId, LocalDateTime start);

    @Query(value = "select count(event_id) from event_registration where participant_id = ?1 and is_declined = true and approve_or_reject_time >= ?2", nativeQuery = true)
    int getNumberOfRejectsForUser(String userId, LocalDateTime start);

    @Query(value = "select count(event_id) from event_registration where participant_id = ?1 and is_approved = true and approve_or_reject_time >= ?2", nativeQuery = true)
    int getNumberOfAcceptsForUser(String userId, LocalDateTime start);

    @Query(value = "select count(er.event_id) from event_registration er \n" +
            "inner join events e\n" +
            "on e.event_id = er.event_id\n" +
            "where\n" +
            "er.participant_id = ?1 and e.event_status = 'Finished' and e.end_time >= ?2", nativeQuery = true)
    int getNumberOfFinishedEventsForUser(String userId, LocalDateTime start);

    @Query(value = "SELECT * FROM event_registration WHERE event_id = ?1 AND is_approved IS TRUE AND is_declined IS NOT TRUE", nativeQuery = true)
    List<EventRegistration> getApprovedRequests(long eventId);

    @Query(value = "SELECT u.email_id FROM event_registration er \n" +
            "inner join user_info u \n" +
            "on er.participant_id = u.user_id " +
            "where er.event_id = ?1", nativeQuery = true)
    List<String> getEventSignupsForCancellationEmail(long eventId);
}
