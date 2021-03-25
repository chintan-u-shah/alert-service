package com.wheelsup.alertservice.external.api;

import com.wheelsup.alertservice.external.dto.FileGeneratorRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "file-generator-service")
public interface FileGeneratorClient {
        @PostMapping(value = "/generate")
        ResponseEntity<byte[]> generateFileContent(@RequestBody FileGeneratorRequest fileGeneratorRequest);

}
