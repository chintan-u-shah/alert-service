package com.wheelsup.alertservice.external.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlertRequest {
    private String alertType;
    private Boolean isAlertDismissed;
}
