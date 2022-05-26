package com.cmpe275.cloudeventcenter.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import com.cmpe275.cloudeventcenter.utils.Enum;
import javax.persistence.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "REVIEW")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REVIEW_ID")
    private long reviewId;

    @OneToOne
    @JoinColumn(name = "REVIEWEE_ID")
    private UserInfo reviewee;

    @OneToOne
    @JoinColumn(name = "REVIEWER_ID")
    private UserInfo reviewer;

    @OneToOne
    @JoinColumn(name = "EVENT_ID")
    @JsonIgnoreProperties({"userInfo"})
    private Event event;

    @Enumerated(EnumType.STRING)
    private Enum.RevieweeType revieweeType;

    @Column(name = "RATING")
    private int rating;

    @Column(name = "TEXT_FEEDBACK")
    private String textFeedback;

}
