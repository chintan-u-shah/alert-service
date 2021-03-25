package com.wheelsup.alertservice.external.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserData {

    private Integer userId;
    private String userName;
    private Integer memberId;
    private String phoneNumber;
    private String memberType;
    private String subject;

}
