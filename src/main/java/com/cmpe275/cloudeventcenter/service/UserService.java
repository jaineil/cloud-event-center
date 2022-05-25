package com.cmpe275.cloudeventcenter.service;

import com.cmpe275.cloudeventcenter.model.UserInfo;
import com.cmpe275.cloudeventcenter.repository.UserRepository;
import com.cmpe275.cloudeventcenter.utils.Enum;
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

    public UserInfo getUserInfo(String userId) {
        UserInfo fetchedUser = userRepository.findUserInfoByUserId(userId);
        return fetchedUser;
    }

    public void updateUserRating(String userId, int newRating, Enum.RevieweeType revieweeType) {
        if (revieweeType.equals(Enum.RevieweeType.Participant)) {
            UserInfo fetchedUser = userRepository.findUserInfoByUserId(userId);
            float rating = fetchedUser.getParticipantRating();
            int ratingsReceived = fetchedUser.getRatingsReceivedAsParticipant();
            float updatedRating = ((rating * ratingsReceived) + newRating) / (ratingsReceived + 1);
            int updatedRatingsReceived = ratingsReceived + 1;
            fetchedUser.setParticipantRating(updatedRating);
            fetchedUser.setRatingsReceivedAsParticipant(updatedRatingsReceived);
            userRepository.save(fetchedUser);
            return;
        } else {
            UserInfo fetchedUser = userRepository.findUserInfoByUserId(userId);
            float rating = fetchedUser.getOrganizerRating();
            int ratingsReceived = fetchedUser.getRatingsReceivedAsOrganizer();
            float updatedRating = ((rating * ratingsReceived) + newRating) / (ratingsReceived + 1);
            int updatedRatingsReceived = ratingsReceived + 1;
            fetchedUser.setOrganizerRating(updatedRating);
            fetchedUser.setRatingsReceivedAsOrganizer(updatedRatingsReceived);
            userRepository.save(fetchedUser);
            return;
        }

    }

//    public boolean checkIfUserIsOrganizer(String userId){
//
//    }
}
