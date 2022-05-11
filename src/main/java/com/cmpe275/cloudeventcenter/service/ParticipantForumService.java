package com.cmpe275.cloudeventcenter.service;

import com.cmpe275.cloudeventcenter.model.ParticipantForum;
import com.cmpe275.cloudeventcenter.repository.ParticipantForumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParticipantForumService {

    @Autowired
    private ParticipantForumRepository participantForumRepository;

    public long addMessageToSignupForum(ParticipantForum message) {
        long id = participantForumRepository.save(message).getMessageId();
        return id;
    }

    public List<ParticipantForum> getAllMessagesForEvent(long eventId) {
        List<ParticipantForum> messageList = participantForumRepository.findParticipantForumsByEventId(eventId);
        return messageList;
    }
}
