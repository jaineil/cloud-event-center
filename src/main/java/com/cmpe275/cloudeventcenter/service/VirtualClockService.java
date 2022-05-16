package com.cmpe275.cloudeventcenter.service;

import com.cmpe275.cloudeventcenter.model.Event;
import com.cmpe275.cloudeventcenter.model.VirtualClock;
import com.cmpe275.cloudeventcenter.repository.EventRepository;
import com.cmpe275.cloudeventcenter.repository.VirtualClockRepository;
import com.cmpe275.cloudeventcenter.utils.Enum;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
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

     public LocalDateTime getVirtualClock() {
         try {
             long minutesForwarded = virtualClockRepository.findMinutesForwarded();
             LocalDateTime virtualTime = LocalDateTime.now().plusMinutes(minutesForwarded);
             return virtualTime;
         } catch (Exception e) {
             System.out.println(e);
             VirtualClock newVirtualClock = VirtualClock.builder()
                     .minutesForwarded(0)
                     .build();

             virtualClockRepository.save(newVirtualClock);
             long minutesForwarded = virtualClockRepository.findMinutesForwarded();
             LocalDateTime virtualTime = LocalDateTime.now().plusMinutes(minutesForwarded);
             return virtualTime;
         }
//         VirtualClock virtualClock = virtualClockRepository.findTopByOrderByLocalDateTimeDesc();
//
//         if (virtualClock == null) {
//             LocalDateTime local = LocalDateTime.now();
//             VirtualClock realClock = VirtualClock.builder()
//                     .localDateTime(local)
//                     .build();
//             virtualClockRepository.save(realClock);
//             return realClock;
//         } else {
//             return virtualClock;
//         }
     }

     public boolean isValidSimulationTime(LocalDateTime incomingVirtualTime) {
         long newMinutesForwarded = ChronoUnit.MINUTES.between(LocalDateTime.now(), incomingVirtualTime);
         long currentMinutesForwarded = virtualClockRepository.findMinutesForwarded();
         if (currentMinutesForwarded <= newMinutesForwarded) {
             long minutesInAYear = 525600;
             long timeAdvancement = newMinutesForwarded - currentMinutesForwarded;
             if (timeAdvancement > minutesInAYear) {
                 return false;
             } else {
                 return true;
             }
         } else {
             return false;
         }
//         VirtualClock virtualClock = virtualClockRepository.findTopByOrderByLocalDateTimeDesc();
//         LocalDateTime lastVirtualTime = virtualClock.getLocalDateTime();
//         long delta = lastVirtualTime.until(incomingVirtualTime, ChronoUnit.HOURS);
//         System.out.println("Delta: " + delta);
//         long year = 365 * 24;
//         if (delta < 0 || delta > year) {
//             return false;
//         } else return true;
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

         long minutesForwarded = ChronoUnit.MINUTES.between(LocalDateTime.now(), virtualTime);

         VirtualClock updatedVirtualClock = VirtualClock.builder()
                 .minutesForwarded(minutesForwarded)
                 .build();

         virtualClockRepository.save(updatedVirtualClock);
     }

    @Transactional
    public void simulateCron() {
         LocalDateTime virtualTime = getVirtualClock();
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
    }
}
