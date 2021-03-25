package com.wheelsup.alertservice.external.api;

import com.wheelsup.alertservice.external.dto.EmailRequest;
import com.wheelsup.common.util.GenericResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "email-service")
public interface EmailServiceClient {

    @PostMapping(value = "/email-request/fire")
    GenericResponse<?> fireEmailNotification(@RequestBody EmailRequest request);
}
