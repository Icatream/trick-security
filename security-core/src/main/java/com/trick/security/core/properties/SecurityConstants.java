package com.trick.security.core.properties;

public interface SecurityConstants {

    String DEFAULT_PUBLIC_RESOURCES = "/pub/*";

    String DEFAULT_LOGOUT_URI = "/logout";

    String DEFAULT_VERIFICATION_CODE_URI_PREFIX = "/verify";

    String DEFAULT_UNAUTHENTICATED_URI = "/authentication/require";

    String DEFAULT_SESSION_INVALID_URI = "/session/invalid";

    String DEFAULT_LOGIN_PROCESSING_URI_FORM = "/authentication/form";

    String DEFAULT_LOGIN_PROCESSING_URI_SMS = "/authentication/sms";

    String DEFAULT_LOGIN_PROCESSING_URL_OPENID = "/authentication/openid";

    String DEFAULT_PARAMETER_NAME_PHONE = "phone";

    String DEFAULT_PARAMETER_NAME_PROVIDERID = "providerId";

    String DEFAULT_PARAMETER_NAME_OPENID = "openId";
}
