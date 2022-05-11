package com.cmpe275.cloudeventcenter.service;

import com.cmpe275.cloudeventcenter.model.UserInfo;
import com.cmpe275.cloudeventcenter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void insertUserInfo(UserInfo userInfo) {
        try {
            userRepository.save(userInfo);
            return;
        } catch (Exception e) {
            System.err.println(e);
            return;
        }
    }
}