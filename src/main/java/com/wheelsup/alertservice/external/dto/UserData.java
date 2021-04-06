package com.wheelsup.alertservice.external.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserData {

    private Long userId;
    private String userName;
    private Long memberId;
    private String phoneNumber;
    private String memberType;
    private String subject;
    private String legalName;
    private String userLocation;
    private String inquiryType;
    private String copyrightYear;

}
