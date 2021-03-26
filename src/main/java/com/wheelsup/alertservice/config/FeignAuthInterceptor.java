package com.wheelsup.alertservice.config;

import com.wheelsup.alertservice.util.WuSecurityUtils;
import com.wheelsup.common.util.AuthUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class FeignAuthInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        template.header(HttpHeaders.AUTHORIZATION, WuSecurityUtils.buildBearerAuthorization(AuthUtil.getToken(null)));
        template.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        template.header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
    }
}
