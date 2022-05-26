package com.cmpe275.cloudeventcenter.repository;

import com.cmpe275.cloudeventcenter.model.Event;
import com.cmpe275.cloudeventcenter.model.UserInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends CrudRepository<Event, Long> {
    List<Event> findAll();
    Event getEventByEventId(long eventId);
    List<Event> getAllByDeadlineBefore(LocalDateTime time);
    List<Event> getAllByUserInfo(UserInfo userInfo);
//    List<Event> findAllByStartTimeAfter(LocalDateTime from);
    List<Event> findAllByCreationTimeAfter(LocalDateTime from);
//    List<Event> findAllByStartTimeAfterAndFeeGreaterThan(LocalDateTime from, double fee);
    List<Event> findAllByCreationTimeAfterAndFeeGreaterThan(LocalDateTime from, double fee);

    @Query(value = "select coalesce(count(event_id),0) from events where event_status = ?1 and deadline >= ?2", nativeQuery = true)
    Integer getNumberOfCancelledEvents(String status, LocalDateTime from);

    @Query(value = "with \n" +
            "cte\n" +
            "as\n" +
            "(select e.event_id, e.min_participants from events e where e.event_status = ?1 and e.deadline >= ?2),\n" +
            "cte1\n" +
            "as\n" +
            "(\n" +
            "\tselect er.event_id, count(*) as event_participants from event_registration er group by er.event_id\n" +
            ")\n" +
            "select COALESCE(ROUND((sum(c1.event_participants) / sum(c.min_participants)),2),0.0) as registered_to_min_participants\n" +
            "from cte c\n" +
            "inner join cte1 c1\n" +
            "on\n" +
            "c.event_id = c1.event_id;",
            nativeQuery = true)
    BigDecimal getEventParticipantsToMinRequirementsRatio(String status, LocalDateTime from);

    @Query(value = "select coalesce(count(event_id),0) from events where event_status = ?1 and end_time >= ?2", nativeQuery = true)
    Integer getNumberOfFinishedEvents(String status, LocalDateTime from);

    @Query(value = "with\n" +
            "cte\n" +
            "as\n" +
            "(\n" +
            "  select e.event_id from events e where e.event_status = ?1 and e.end_time >= ?2\n" +
            "),\n" +
            "cte1\n" +
            "as\n" +
            "(\n" +
            "  select er.event_id, count(*) as event_participants from event_registration er group by er.event_id\n" +
            ")\n" +
            "select COALESCE(ROUND((sum(cte1.event_participants) / count(cte.event_id)),2),0.0) as average_number_of_participants from cte\n" +
            "inner join cte1\n" +
            "on cte.event_id = cte1.event_id;", nativeQuery = true)
    BigDecimal getAverageEventParticipantsInFinishedEvents(String status, LocalDateTime from);

    @Query(value = "select COALESCE(count(*),0) from events where organizer_id = ?1 and creation_time >= ?2", nativeQuery = true)
    Integer getNumberOfEventsCreatedForOrganizer(String userId, LocalDateTime start);

    @Query(value = "select COALESCE(count(*),0) from events where organizer_id = ?1 and fee > 0 and creation_time > ?2", nativeQuery = true)
    Integer getNumberOfPaidEventsCreatedForOrganizer(String userId, LocalDateTime start);

    @Query(value = "select COALESCE(count(*),0) from events where organizer_id = ?1 and event_status = 'Cancelled' and deadline = ?2", nativeQuery = true)
    Integer getNumberOfCancelledEventsForOrganizer(String userId, LocalDateTime start);

    @Query(value = "select COALESCE(ROUND((sum(t.total_participants) / sum(t.total_min_participants)),2), 0.0) as ratio from (select e.event_id, count(er.participant_id) as total_participants, e.min_participants as total_min_participants from events e\n" +
            "inner join event_registration er\n" +
            "on\n" +
            "e.event_id = er.event_id\n" +
            "where e.organizer_id = ?1 \n" +
            "and deadline >= ?2 \n" +
            "group by e.event_id) t;", nativeQuery = true)
    Float getRatioOfRegisteredToMinimumParticipantsForCancelledEvents(String userId, LocalDateTime start);

    @Query(value = "select COALESCE(count(*), 0) from events where organizer_id = ?1 and event_status = 'Finished' and end_time >= ?2", nativeQuery = true)
    Integer getNumberOfFinishedEventsForOrganizer(String userId, LocalDateTime start);

    @Query(value = "with\n" +
            "cte\n" +
            "as\n" +
            "(select event_id from events where organizer_id = ?1 and event_status = 'Finished' and end_time >= ?2),\n" +
            "cte1\n" +
            "as\n" +
            "(select participant_id, event_id from event_registration)\n" +
            "select COALESCE(ROUND(count(cte1.participant_id) / count(distinct cte.event_id),2),0.0) as average\n" +
            "from cte\n" +
            "inner join cte1\n" +
            "on cte.event_id = cte1.event_id;", nativeQuery = true)
    BigDecimal getAverageNumberOfParticipantsForFinishedEventsForOrganizer(String userId, LocalDateTime start);

    @Query(value = "select COALESCE(count(*), 0) from events where organizer_id = ?1 and event_status = 'Finished' and end_time >= ?2 and fee > 0;", nativeQuery = true)
    Integer getNumberOfPaidAndFinishedEventsForOrganizer(String userId, LocalDateTime start);

    @Query(value = "select COALESCE(sum(fee), 0) as revenue from events where organizer_id = ?1 and event_status = 'Finished' and end_time >= ?2 and fee > 0;", nativeQuery = true)
    Integer getRevenueOfPaidAndFinishedEventsForOrganizer(String userId, LocalDateTime start);
}

