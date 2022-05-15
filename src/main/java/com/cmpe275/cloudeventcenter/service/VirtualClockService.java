package com.cmpe275.cloudeventcenter.service;

import com.cmpe275.cloudeventcenter.model.Event;
import com.cmpe275.cloudeventcenter.model.VirtualClock;
import com.cmpe275.cloudeventcenter.repository.EventRepository;
import com.cmpe275.cloudeventcenter.repository.VirtualClockRepository;
import com.cmpe275.cloudeventcenter.utils.Enum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class VirtualClockService {

    @Autowired
    VirtualClockRepository virtualClockRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    EventService eventService;

    @Autowired
    EventRegistrationService eventRegistrationService;

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

     @Transactional
     public void simulate(LocalDateTime virtualTime) {
         List<Event> events = eventService.getAllEventsByDeadline(virtualTime);

         for (Event event: events) {
             System.out.println("--------------------------------------------------------------------------------");
             System.out.println("Event ID: " + event.getEventId());

             long delta = event.getEndTime().until(virtualTime, ChronoUnit.HOURS ); // difference in hours
             System.out.println("Difference in hours between event end datetime and virtual datetime: " + delta);

             // event end time is before or equal to virtual time
             if (delta >= 0) {
                 if (event.getEventStatus() == Enum.EventStatus.Cancelled) {
                     continue;
                 }

                 int eventRegistrationCount = eventRegistrationService.getEventRegistrationCount(event.getEventId());
                 System.out.println("Number of registered users: " + eventRegistrationCount);

                 if (eventRegistrationCount < event.getMinParticipants()) {
                     System.out.println("Number of registered users is less than min participants");
                     System.out.println("Canceling the event");
                     event.setEventStatus(Enum.EventStatus.Cancelled);
                     if (event.getIsSignUpForumReadOnly() == false) {
                         event.setIsSignUpForumReadOnly(true);
                     }
                     if (event.getIsParticipantForumReadOnly() == false) {
                         event.setIsParticipantForumReadOnly(true);
                     }
                     event.setParticipantForumStatus(Enum.ParticipantForumStatus.ClosedBecauseEventCancelled);

                 } else {
                     System.out.println("Finishing the event");
                     event.setEventStatus(Enum.EventStatus.Finished);
                     if (event.getIsSignUpForumReadOnly() == false) {
                         event.setIsSignUpForumReadOnly(true);
                     }
                     if (delta >= 72) {
                         if (event.getIsParticipantForumReadOnly() == false) {
                            event.setIsParticipantForumReadOnly(true);
                        }
                         if (!event.getParticipantForumStatus().equals(Enum.ParticipantForumStatus.ClosedBecauseManuallyClosed)) {
                             event.setParticipantForumStatus(Enum.ParticipantForumStatus.ClosedBecauseAutomatic72HourExpiry);
                         }
                     }
                 }
             }

             // event end time is after virtual time
             if (delta < 0) {
                 if (event.getEventStatus() == Enum.EventStatus.Cancelled) {
                     continue;
                 }

                 int eventRegistrationCount = eventRegistrationService.getEventRegistrationCount(event.getEventId());
                 System.out.println("Number of registered users: " + eventRegistrationCount);

                 if (eventRegistrationCount < event.getMinParticipants()) {
                     System.out.println("Number of registered users is less than min participants");
                     System.out.println("Canceling the event");
                     event.setEventStatus(Enum.EventStatus.Cancelled);
                     if (event.getIsSignUpForumReadOnly() == false) {
                         event.setIsSignUpForumReadOnly(true);
                     }
                     if (event.getIsParticipantForumReadOnly() == false) {
                         event.setIsParticipantForumReadOnly(true);
                     }
                     event.setParticipantForumStatus(Enum.ParticipantForumStatus.ClosedBecauseEventCancelled);
                 } else {
                     long eventStatusFlag = event.getStartTime().until(virtualTime, ChronoUnit.HOURS ); // difference in hours
                     if (eventStatusFlag < 0) {
                         event.setEventStatus(Enum.EventStatus.SignUpClosed);
                     }
                     if (eventStatusFlag >= 0) {
                         event.setEventStatus(Enum.EventStatus.Ongoing);
                     }
                     if (event.getIsSignUpForumReadOnly() == false) {
                         event.setIsSignUpForumReadOnly(true);
                     }
                     event.setParticipantForumStatus(Enum.ParticipantForumStatus.Open);
                 }
             }

             eventRepository.save(event);
         }

         VirtualClock updatedVirtualClock = VirtualClock.builder()
                 .localDateTime(virtualTime)
                 .build();

         virtualClockRepository.save(updatedVirtualClock);

         return;
     }
}
