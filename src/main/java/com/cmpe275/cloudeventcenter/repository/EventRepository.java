package com.cmpe275.cloudeventcenter.repository;

import com.cmpe275.cloudeventcenter.model.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends CrudRepository<Event, Long> {
}
