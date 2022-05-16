package com.cmpe275.cloudeventcenter.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "VIRTUAL_CLOCK")
public class VirtualClock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VIRTUAL_CLOCK_ID")
    private long virtualClockId;

    @Column(name = "VIRTUAL_TIME", columnDefinition = "TIMESTAMP")
    private LocalDateTime localDateTime;

    @Column(name = "MINUTES_FORWARDED")
    private long minutesForwarded = 0;
}
