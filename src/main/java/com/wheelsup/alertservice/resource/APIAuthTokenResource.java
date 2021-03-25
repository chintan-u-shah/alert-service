package com.wheelsup.alertservice.resource;

import com.wheelsup.common.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class APIAuthTokenResource {
	public String getAccessToken() {
		return AuthUtil.getToken(SecurityContextHolder.getContext().getAuthentication());
	}
}