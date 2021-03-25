package com.wheelsup.alertservice.util;

public final class WuSecurityUtils {
	private WuSecurityUtils() {
	}
	public static String buildBearerAuthorization(String accessToken) {
		return "Bearer " + accessToken;
	}
}