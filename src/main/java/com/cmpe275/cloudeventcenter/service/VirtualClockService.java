package com.cmpe275.cloudeventcenter.service;

import com.cmpe275.cloudeventcenter.model.VirtualClock;
import com.cmpe275.cloudeventcenter.repository.VirtualClockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VirtualClockService {

    @Autowired
    VirtualClockRepository virtualClockRepository;

     public VirtualClock getVirtualClock() {
         VirtualClock virtualClock = virtualClockRepository.findTopByOrderByLocalDateTimeDesc();

         if (virtualClock == null) {
             LocalDateTime local = LocalDateTime.now();
             VirtualClock realClock = VirtualClock.builder()
                     .localDateTime(local)
                     .build();
             return realClock;
         } else {
             return virtualClock;
         }
     }
}
