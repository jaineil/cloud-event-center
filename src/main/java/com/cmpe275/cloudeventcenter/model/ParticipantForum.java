package com.cmpe275.cloudeventcenter.model;

import com.cmpe275.cloudeventcenter.model.UserInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PARTICIPANT_FORUM")

public class ParticipantForum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MESSAGE_ID")
    private long messageId;

    @OneToOne
    @JoinColumn(name = "SENDER_ID")
    @JsonIgnoreProperties({"address", "gender", "description"})
    private UserInfo userInfo;

    @Column(name = "TIMESTAMP")
    @CreationTimestamp
    private LocalDateTime timestamp;

    @Column(name = "MESSAGE_TEXT")
    private String messageText;

    @Column(name = "IMAGE_URL")
    private String imageUrl;

    @Column(name = "EVENT_ID")
    private long eventId;
}
