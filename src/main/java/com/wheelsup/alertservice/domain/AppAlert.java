package com.wheelsup.alertservice.domain;

import com.wheelsup.common.jpa.Auditable;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(catalog = "wheelsup", name = "AppAlerts")
@AllArgsConstructor
@NoArgsConstructor
public class AppAlert extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String alertType;
    private Boolean isAlertNotified;
    private Long clientUserId;
    private Boolean isAlertDismissed;
    private String notifiedGroup;

}
