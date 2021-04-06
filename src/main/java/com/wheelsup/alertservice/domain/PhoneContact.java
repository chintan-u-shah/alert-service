package com.wheelsup.alertservice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(catalog = "wheelsup", name = "PhoneContact")
public class PhoneContact {

    @Id
    @GeneratedValue
    @Column(name = "phoneContactId")
    private Long id;
    private String phoneNumber;
    private Long memberId;

}
