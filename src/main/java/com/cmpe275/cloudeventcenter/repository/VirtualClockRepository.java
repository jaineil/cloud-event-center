package com.cmpe275.cloudeventcenter.repository;

import com.cmpe275.cloudeventcenter.model.VirtualClock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VirtualClockRepository extends CrudRepository<VirtualClock, Long> {
    VirtualClock findTopByOrderByLocalDateTimeDesc();
}
