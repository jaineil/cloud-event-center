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

    @Column(name = "IMAGE_URL")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private Enum.AdmissionPolicy admissionPolicy;

    @Enumerated(EnumType.STRING)
    private Enum.EventStatus eventStatus;

}


//public class Player {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "ID")
//    private long id;
//
//    @Column(name = "FIRST_NAME", nullable = false)
//    private String firstName;
//
//    @Column(name = "LAST_NAME", nullable = false)
//    private String lastName;
//
//    @Column(name = "EMAIL", nullable = false, unique = true)
//    private String email;
//
//    @Column(name = "DESCRIPTION")
//    private String description;
//
//    @Embedded
//    @AttributeOverrides({
//            @AttributeOverride( name = "street", column = @Column(name = "STREET")),
//            @AttributeOverride( name = "city", column = @Column(name = "CITY")),
//            @AttributeOverride( name = "zip", column = @Column(name = "ZIP")),
//            @AttributeOverride( name = "state", column = @Column(name = "STATE"))
//    })
//    private Address address;
//
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "TEAM_ID")
//    @JsonIgnoreProperties({"players", "address"})
//    private Team team;
//
//    @ManyToMany(fetch = FetchType.EAGER)
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    @JoinTable(name="OPPONENT",
//            joinColumns=@JoinColumn(name="playerId"),
//            inverseJoinColumns=@JoinColumn(name="opponentId")
//    )
//    @JsonIgnoreProperties({"address", "team", "opponents"})
//    private List<Player> opponents;
//
//}

