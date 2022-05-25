package com.cmpe275.cloudeventcenter.repository;

import com.cmpe275.cloudeventcenter.model.Event;
import com.cmpe275.cloudeventcenter.model.UserInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface EventRepository extends CrudRepository<Event, Long> {
    List<Event> findAll();
    Event getEventByEventId(long eventId);
    List<Event> getAllByDeadlineBefore(LocalDateTime time);
    List<Event> getAllByUserInfo(UserInfo userInfo);
    List<Event> findAllByStartTimeAfter(LocalDateTime from);
    List<Event> findAllByStartTimeAfterAndFeeGreaterThan(LocalDateTime from, double fee);

    @Query(value = "select count(event_id) from events where event_status = ?1 and deadline >= ?2", nativeQuery = true)
    int getNumberOfCancelledEvents(String status, LocalDateTime from);

    @Query(value = "with \n" +
            "cte\n" +
            "as\n" +
            "(select e.event_id, e.min_participants from events e where e.event_status = ?1 and e.deadline >= ?2),\n" +
            "cte1\n" +
            "as\n" +
            "(\n" +
            "\tselect er.event_id, count(*) as event_participants from event_registration er group by er.event_id\n" +
            ")\n" +
            "select sum(c.min_participants) as total_min_participants, sum(c1.event_participants) as total_event_participants, ROUND((sum(c1.event_participants) / sum(c.min_participants)),2) as registered_to_min_participants\n" +
            "from cte c\n" +
            "inner join cte1 c1\n" +
            "on\n" +
            "c.event_id = c1.event_id;",
            nativeQuery = true)
    Map<String, BigDecimal> getEventParticipantsToMinRequirementsRatio(String status, LocalDateTime from);
}

