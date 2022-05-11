package com.cmpe275.cloudeventcenter.repository;

import com.cmpe275.cloudeventcenter.model.UserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserInfo, Long> {
    UserInfo findUserInfoByUserId(String userId);
}
