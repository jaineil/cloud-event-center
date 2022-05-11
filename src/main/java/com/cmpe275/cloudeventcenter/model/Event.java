package com.cmpe275.cloudeventcenter.model;

import com.cmpe275.cloudeventcenter.utils.Enum;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

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

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DESCRIPTION")
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

    @Enumerated(EnumType.STRING)
    private Enum.AdmissionPolicy admissionPolicy;

    @Enumerated(EnumType.STRING)
    private Enum.EventStatus eventStatus;

}