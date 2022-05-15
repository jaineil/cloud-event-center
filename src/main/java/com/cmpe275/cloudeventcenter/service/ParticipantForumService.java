package com.cmpe275.cloudeventcenter.service;

import com.cmpe275.cloudeventcenter.model.Event;
import com.cmpe275.cloudeventcenter.model.ParticipantForum;
import com.cmpe275.cloudeventcenter.repository.EventRepository;
import com.cmpe275.cloudeventcenter.repository.ParticipantForumRepository;
import com.cmpe275.cloudeventcenter.utils.Enum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParticipantForumService {

    @Autowired
    private ParticipantForumRepository participantForumRepository;

    @Autowired
    private EventRepository eventRepository;

    public long addMessageToSignupForum(ParticipantForum message) {
        long id = participantForumRepository.save(message).getMessageId();
        return id;
    }

    public List<ParticipantForum> getAllMessagesForEvent(long eventId) {
        List<ParticipantForum> messageList = participantForumRepository.findParticipantForumsByEventId(eventId);
        return messageList;
    }

    public void manuallyCloseParticipantForum(Event event) {
        event.setParticipantForumStatus(Enum.ParticipantForumStatus.ClosedBecauseManuallyClosed);
        eventRepository.save(event);
    }
}
