package com.trick.security.core.properties.browser.session;

import com.trick.security.core.properties.SecurityConstants;

public class SessionProperties {

    private String sessionInvalidUri = SecurityConstants.DEFAULT_SESSION_INVALID_URI;

    private int maximumSessions = 1;

    private boolean maxSessionsPreventsLogin = false;

    public String getSessionInvalidUri() {
        return sessionInvalidUri;
    }

    public void setSessionInvalidUri(String sessionInvalidUri) {
        this.sessionInvalidUri = sessionInvalidUri;
    }

    public int getMaximumSessions() {
        return maximumSessions;
    }

    public void setMaximumSessions(int maximumSessions) {
        this.maximumSessions = maximumSessions;
    }

    public boolean isMaxSessionsPreventsLogin() {
        return maxSessionsPreventsLogin;
    }

    public void setMaxSessionsPreventsLogin(boolean maxSessionsPreventsLogin) {
        this.maxSessionsPreventsLogin = maxSessionsPreventsLogin;
    }
}
