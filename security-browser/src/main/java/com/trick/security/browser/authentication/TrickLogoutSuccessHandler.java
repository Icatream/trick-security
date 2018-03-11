package com.trick.security.browser.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trick.security.core.properties.SecurityProperties;
import com.trick.security.core.properties.browser.BrowserProperties;
import com.trick.security.core.support.SimpleResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class TrickLogoutSuccessHandler implements LogoutSuccessHandler {

    private static Logger logger = LoggerFactory.getLogger(TrickLogoutSuccessHandler.class);
    private final BrowserProperties browserProperties;
    private final ObjectMapper objectMapper;

    @Autowired
    public TrickLogoutSuccessHandler(SecurityProperties securityProperties, ObjectMapper objectMapper){
        this.browserProperties = securityProperties.getBrowser();
        this.objectMapper = objectMapper;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        logger.info("退出成功");
        String logoutSuccessUri = browserProperties.getLogoutSuccessUrl();
        if (StringUtils.isBlank(logoutSuccessUri)) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(new SimpleResponse("退出成功")));
        } else {
            response.sendRedirect(logoutSuccessUri);
        }
    }
}
