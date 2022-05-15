package com.cmpe275.cloudeventcenter.model;

import com.cmpe275.cloudeventcenter.utils.Enum;
import lombok.*;
import org.apache.catalina.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "EVENTS")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EVENT_ID")
    private long eventId;

    @OneToOne
    @JoinColumn(name = "ORGANIZER_ID")
    private UserInfo userInfo;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DESCRIPTION" , length = 3000)
    private String description;

    @Column(name = "START_TIME", columnDefinition = "TIMESTAMP")
    private LocalDateTime startTime;

    @Column(name = "END_TIME", columnDefinition = "TIMESTAMP")
    private LocalDateTime endTime;

    @Column(name = "DEADLINE", columnDefinition = "TIMESTAMP")
    private LocalDateTime deadline;

    @OneToOne
    @JoinColumn(name = "ADDRESS_ID")
    private Address address;

    @Column(name = "MIN_PARTICIPANTS")
    private int minParticipants;

    @Column(name = "MAX_PARTICIPANTS")
    private int maxParticipants;

    @Column(name = "FEE")
    private double fee;

    @Column(name = "IMAGE_URL")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private Enum.AdmissionPolicy admissionPolicy;

    @Enumerated(EnumType.STRING)
    private Enum.EventStatus eventStatus;

    @Column(name = "IS_SIGNUP_FORUM_READ_ONLY", columnDefinition = "boolean default false")
    private Boolean isSignUpForumReadOnly;

    @Column(name = "IS_PARTICIPANT_FORUM_READ_ONLY", columnDefinition = "boolean default false")
    private Boolean isParticipantForumReadOnly;

    @Enumerated(EnumType.STRING)
    private Enum.ParticipantForumStatus participantForumStatus;

    @Transient
    private int registrationCount;

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "eventId")
//    private List<EventRegistration> eventRegistrationList;

}