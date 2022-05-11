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
@Table(name = "EVENT_REGISTRATION")
public class EventRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REGISTRATION_ID")
    private long registrationId;

//    @ManyToOne(fetch = FetchType.LAZY)
    @OneToOne
    @JoinColumn(name = "EVENT_ID")
    private Event event;

//    @ManyToOne(fetch = FetchType.LAZY)
    @OneToOne
    @JoinColumn(name = "PARTICIPANT_ID")
    private UserInfo userInfo;

    @Column(name = "IS_APPROVED")
    private Boolean isApproved;

    @Column(name = "IS_PAID")
    private Boolean isPaid;
}