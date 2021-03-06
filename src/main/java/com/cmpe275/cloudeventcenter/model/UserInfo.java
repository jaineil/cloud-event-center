package com.cmpe275.cloudeventcenter.model;

import com.cmpe275.cloudeventcenter.utils.Enum;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "USER_INFO")
public class UserInfo {
    @Id
    @Column(name = "USER_ID", unique = true)
    private String userId; // received from Firebase

    @Column(name = "EMAIL_ID", unique = true)
    private String emailId; // received from Firebase

    @Column(name = "ACCOUNT_TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private Enum.AccountType accountType;

    @Column(name = "FULL_NAME", nullable = false)
    private String fullName;

    @Column(name = "SCREEN_NAME", unique = true)
    private String screenName;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "DESCRIPTION", length = 3000)
    private String description;

    @Column(name = "AVG_PARTICIPANT_RATING", columnDefinition = "float default 0.00", precision = 2)
    private float participantRating;

    @Column(name = "AVG_ORGANIZER_RATING", columnDefinition = "float default 0.00", precision = 2)
    private float organizerRating;

    @Column(name = "RATINGS_RECEIVED_AS_PARTICIPANT", columnDefinition = "int default 0")
    private int ratingsReceivedAsParticipant;

    @Column(name = "RATINGS_RECEIVED_AS_ORGANIZER", columnDefinition = "int default 0")
    private int ratingsReceivedAsOrganizer;

    @OneToOne
    @JoinColumn(name = "ADDRESS_ID")
    private Address address;

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userId")
//    private List<EventRegistration> eventRegistrationList;
}
