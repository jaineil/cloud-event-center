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

    public boolean checkIfUserExists(String userId) {
        try {
            UserInfo userInfo = userRepository.findUserInfoByUserId(userId);
            if (userInfo == null) {
                return false;
            } else return true;
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
    }

    public UserInfo getUserByUserId(String userId) {
        return userRepository.findUserInfoByUserId(userId);
    }
}
