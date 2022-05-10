package com.cmpe275.cloudeventcenter.model;

import com.cmpe275.cloudeventcenter.utils.Enum;
import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "USER_INFO")
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private long userId; // received from Firebase

    @Enumerated(EnumType.STRING)
    private Enum.AccountType accountType;

    @Column(name = "FULL_NAME")
    private String fullName;

    @Column(name = "SCREEN_NAME")
    private String screenName;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "DESCRIPTION")
    private String description;

    @OneToOne
    @JoinColumn(name = "ADDRESS_ID")
    private Address address;
}
