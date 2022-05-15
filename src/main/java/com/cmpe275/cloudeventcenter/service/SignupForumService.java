package com.cmpe275.cloudeventcenter.service;

import com.cmpe275.cloudeventcenter.model.SignupForum;
import com.cmpe275.cloudeventcenter.repository.SignupForumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SignupForumService {

    @Autowired
    private SignupForumRepository signupForumRepository;

    public long addMessageToSignupForum(SignupForum message) {
        long id = signupForumRepository.save(message).getMessageId();
        return id;
    }

    public List<SignupForum> getAllMessagesForEvent(long eventId) {
        List<SignupForum> messageList = signupForumRepository.findSignupForumsByEventId(eventId);
        return messageList;
    }
}
