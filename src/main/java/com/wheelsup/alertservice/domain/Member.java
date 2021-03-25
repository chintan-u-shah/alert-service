package com.wheelsup.alertservice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(catalog = "wheelsup", name = "member")
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "memberId")
    private Integer memberId;
    private String legalName;

}
