package com.wheelsup.alertservice.config;

import com.wheelsup.alertservice.resource.APIAuthTokenResource;
import com.wheelsup.alertservice.util.WuSecurityUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeignAuthInterceptor implements RequestInterceptor {
    private final APIAuthTokenResource apiAuthTokenResource;
    @Override
    public void apply(RequestTemplate template) {
        template.header(HttpHeaders.AUTHORIZATION, WuSecurityUtils.buildBearerAuthorization(apiAuthTokenResource.getAccessToken()));
        template.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        template.header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
    }
}
