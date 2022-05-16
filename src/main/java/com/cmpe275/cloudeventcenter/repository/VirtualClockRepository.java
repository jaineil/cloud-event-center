package com.cmpe275.cloudeventcenter.repository;

import com.cmpe275.cloudeventcenter.model.VirtualClock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VirtualClockRepository extends CrudRepository<VirtualClock, Long> {
    VirtualClock findTopByOrderByLocalDateTimeDesc();

    @Query(value = "SELECT minutes_forwarded FROM virtual_clock ORDER BY virtual_clock_id DESC LIMIT 1", nativeQuery = true)
    long findMinutesForwarded();
}
