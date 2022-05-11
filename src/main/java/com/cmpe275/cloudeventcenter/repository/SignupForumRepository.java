package com.cmpe275.cloudeventcenter.repository;

import com.cmpe275.cloudeventcenter.model.SignupForum;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SignupForumRepository extends CrudRepository<SignupForum, Long> {
     List<SignupForum> findSignupForumsByEventId(long eventId);
}
