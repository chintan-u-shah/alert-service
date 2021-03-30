package com.wheelsup.alertservice.external.api;

import com.wheelsup.alertservice.external.dto.HtmlFileGeneratorRequest;
import com.wheelsup.common.util.GenericResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "file-generator-service")
public interface FileGeneratorClient {
        @PostMapping(value = "/generate-html")
        GenericResponse<String> generateFileContent(@RequestBody HtmlFileGeneratorRequest fileGeneratorRequest);

}
