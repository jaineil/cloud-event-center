package com.cmpe275.cloudeventcenter.repository;

import com.cmpe275.cloudeventcenter.model.ParticipantForum;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantForumRepository extends CrudRepository<ParticipantForum, Long> {
    List<ParticipantForum> findParticipantForumsByEventId(long eventId);
}
