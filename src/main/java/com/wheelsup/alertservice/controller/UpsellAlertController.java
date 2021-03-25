package com.wheelsup.alertservice.controller;

import com.wheelsup.alertservice.service.UpsellAlertService;
import com.wheelsup.common.util.GenericResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/alert")
@RequiredArgsConstructor

public class UpsellAlertController {

        private final UpsellAlertService upsellAlertService;

        @PostMapping("/upsell")
        //@PreAuthorize("hasAnyRole(@AdminRoles.ROLE_SUPER)")
        public GenericResponse<?> sendUpsellAlert() {
            return upsellAlertService.sendAlert();
        }

}
