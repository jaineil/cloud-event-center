package com.cmpe275.cloudeventcenter.repository;

import com.cmpe275.cloudeventcenter.model.Event;
import com.cmpe275.cloudeventcenter.model.UserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends CrudRepository<Event, Long> {
    List<Event> findAll();
    Event getEventByEventId(long eventId);
    List<Event> getAllByDeadlineBefore(LocalDateTime time);
    List<Event> getAllByUserInfo(UserInfo userInfo);
    List<Event> findAllByStartTimeAfter(LocalDateTime from);
    List<Event> findAllByStartTimeAfterAndFeeGreaterThan(LocalDateTime from, double fee);
}

