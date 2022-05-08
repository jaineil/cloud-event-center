package com.cmpe275.cloudeventcenter.service;

import com.cmpe275.cloudeventcenter.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;
}
